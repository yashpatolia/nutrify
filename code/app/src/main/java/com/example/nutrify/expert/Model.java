package com.example.nutrify.expert;

import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model implements Expert{

    private Evaluator evaluator;

    public Model() {
        try{
            this.evaluator = new LoadingModelEvaluatorBuilder()
                    .load(new File("./src/main/java/com/example/nutrify/expert/nutrify.pmml"))
                    .build();

            evaluator.verify();
        } catch (Exception e) {
            evaluator  = null;
            System.out.println("Error has occurred loading in pmml model");
        }

    }

    private Map<String, Object> parse(String question){
        Map<String, Object> inputData = new HashMap<>();
        //initializing input data
        inputData.put("Grams", null);
        inputData.put("Calories", null);
        inputData.put("Protein", null);
        inputData.put("Fat", null);
        inputData.put("Sat.Fat",null);
        inputData.put("Fiber", null);
        inputData.put("Carbs", null);
        String current = "";
        int macro = 0;
        for(int i = 0; i < question.length(); i++){
            if(question.charAt(i) == ','){
                switch (macro){
                    case 0:
                        inputData.put("Grams", Integer.parseInt(current));
                    case 1:
                        inputData.put("Calories", Integer.parseInt(current));
                    case 2:
                        inputData.put("Protein", Integer.parseInt(current));
                    case 3:
                        inputData.put("Fat", Integer.parseInt(current));
                    case 4:
                        inputData.put("Sat.Fat", Integer.parseInt(current));
                    case 5:
                        inputData.put("Fiber", Integer.parseInt(current));
                    case 6:
                        inputData.put("Carbs", Integer.parseInt(current));

                }
                current = "";
                macro ++;
            }
            else{
                current += question.charAt(i);
            }
        }
        return inputData;
    }
    private String predict(String question){

        Map<String, ?> results = evaluator.evaluate(parse(question));

        results = EvaluatorUtil.decodeAll(results);
        return Objects.requireNonNull(results.get("y")).toString();
    }

    @Override
    public String getExpertAnswer(String question) {
        return predict(question);
    }
}
