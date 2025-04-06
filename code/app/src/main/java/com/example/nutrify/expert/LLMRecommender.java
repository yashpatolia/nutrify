package com.example.nutrify.expert;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class LLMRecommender implements Expert {
    private String result = "";

    private String predict(String input) {
        try {
            URL url = new URL("http://10.0.2.2:5000/llm");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"input\": \"%s\"}", input);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] inputBytes = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(inputBytes, 0, inputBytes.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson(
                response.toString(),
                new TypeToken<HashMap<String, String>>() {}.getType()
            );

            result = map.containsKey("recommendation") ? map.get("recommendation") : "No recommendation found.";
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error contacting LLM server.";
        }
    }

    @Override
    public String getExpertAnswer(String input) {
        Thread background = new Thread(() -> predict(input));
        background.start();
        try {
            background.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
