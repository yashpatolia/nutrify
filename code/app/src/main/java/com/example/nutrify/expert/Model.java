package com.example.nutrify.expert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
public class Model implements Expert {

    private String pred ="";
    private String parse(String question){

        String jsonString = "{";
        String current = "";
        int macro = 0;

        for (int i = 0; i < question.length(); i++) {
            if (question.charAt(i) == ',') {

                switch (macro) {
                    case 0:
                        jsonString += "\"Grams\" : " + current;
                        break;
                    case 1:
                        jsonString += ",\"Calories\" : " + current;
                        break;
                    case 2:
                        jsonString += ",\"Protein\" : " + current;
                        break;
                    case 3:
                        jsonString += ",\"Fat\" : " + current;
                        break;
                    case 4:
                        jsonString += ",\"Sat.Fat\" : " + current;
                        break;
                    case 5:
                        jsonString += ",\"Fiber\" : " + current;
                        break;
                    case 6:
                        jsonString += ",\"Carbs\" : " + current + "}";
                        break;
                }
                current = "";
                macro++;
            } else {
                current += question.charAt(i);
            }
        }


        System.out.println(jsonString);
        return jsonString;
    }

    private String predict(String question){
        try {
            URL url = new URL("http://10.0.2.2:5000/predict");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setDoOutput(true);
            String jsonInputString = parse(question);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }


            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson(String.valueOf(response), new TypeToken<HashMap<String, String>>(){}.getType());

            System.out.println("Response: " + response.toString());
            pred = map.get("prediction");
            return map.get("prediction");

        } catch (Exception e) {
           System.out.println("Error " + e);
        }
        return "";

    }

    @Override
    public String getExpertAnswer(String question) {

        Thread background = new Thread(() -> {
            String result = predict(question);
        });
        background.start();
        try {
            background.join();
        }catch(Exception e){
            System.out.println(e);
        }

        return pred;
    }



}
