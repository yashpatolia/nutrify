from flask import Flask, request, jsonify
import joblib

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
    
    
if __name__ == '__main__':
    app.run(debug=True)
