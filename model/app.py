from flask import Flask, request, jsonify
import requests
import math
import joblib


USDA_API_KEY = "un03bboeRQmzOf6WDUjaMPHiJzLTQ7lgfd8oXzte"
OPENROUTER_API_KEY = "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"

app = Flask(__name__)

model = joblib.load("/Users/justinkwinecki/Documents/School/Year3/Semester2/3A04/Project/3A04-D3/model/nutrify.pkl")


@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()

        features = [
            data['Grams'],
            data['Calories'],
            data['Protein'],
            data['Fat'],
            data['Sat.Fat'],
            data['Fiber'],
            data['Carbs']
        ]
        prediction = model.predict([features])

        return jsonify({'prediction': prediction[0]})

    except Exception as e:
        return jsonify({'error': str(e)}), 400
    

# Cache food data here after pulling from USDA
food_database = []

def fetch_usda_foods():
    global food_database
    food_database = []

    for page in range(1, 11):  # ~200 items
        url = f"https://api.nal.usda.gov/fdc/v1/foods/search?query=&pageSize=20&pageNumber={page}&api_key={USDA_API_KEY}"
        res = requests.get(url)
        data = res.json()

        if "foods" in data:
            for item in data["foods"]:
                try:
                    name = item["description"].lower()
                    if "beverage" in name or "alcohol" in name:
                        continue

                    nutrients = {n["nutrientName"]: n["value"] for n in item["foodNutrients"]}
                    food_database.append({
                        "name": item["description"],
                        "calories": nutrients.get("Energy", 0),
                        "protein": nutrients.get("Protein", 0),
                        "fat": nutrients.get("Total lipid (fat)", 0),
                        "carbs": nutrients.get("Carbohydrate, by difference", 0)
                    })
                except:
                    continue


@app.route('/match', methods=['POST'])
def match_macros():
    try:
        data = request.json
        target = {
            "calories": float(data.get("calories", 0)),
            "protein": float(data.get("protein", 0)),
            "fat": float(data.get("fat", 0)),
            "carbs": float(data.get("carbs", 0))
        }

        if not food_database:
            fetch_usda_foods()

        if not food_database:
            return jsonify({"recommendation": "No food data available."})

        def distance(food):
            return math.sqrt(
                (food["calories"] - target["calories"]) ** 2 +
                (food["protein"] - target["protein"]) ** 2 +
                (food["fat"] - target["fat"]) ** 2 +
                (food["carbs"] - target["carbs"]) ** 2
            )

        closest = min(food_database, key=distance)

        return jsonify({
            "recommendation": f"{closest['name']} (Calories: {closest['calories']}, Protein: {closest['protein']}g, Carbs: {closest['carbs']}g, Fat: {closest['fat']}g)"
        })

    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/llm', methods=['POST'])
def llm_recommend():
    try:
        data = request.json
        prompt = data.get("input", "").strip()

        if not prompt:
            return jsonify({"recommendation": "No input provided."})

        full_prompt = f"Suggest one food item that fits these macros or request: {prompt}. Just return the food name."

        headers = {
            "Authorization": f"Bearer {OPENROUTER_API_KEY}",
            "Content-Type": "application/json"
        }

        body = {
            "model": "openai/gpt-3.5-turbo",
            "messages": [{"role": "user", "content": full_prompt}],
        }

        response = requests.post("https://openrouter.ai/api/v1/chat/completions", headers=headers, json=body)
        result = response.json()
        reply = result["choices"][0]["message"]["content"].strip()

        return jsonify({"recommendation": reply})

    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/refresh', methods=['GET'])
def refresh_foods():
    try:
        fetch_usda_foods()
        return jsonify({"message": f"Loaded {len(food_database)} foods from USDA."})
    except Exception as e:
        return jsonify({"error": str(e)})


if __name__ == '__main__':
    fetch_usda_foods()
    app.run(debug=True)
