package com.example.nutrify.question;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/*
public class AskAQuestion {

    // Method to allow the user to input a question and save it to a CSV file
    public String askUserForQuestion(String question) {
        // Logic to receive the question from the user
        // Save the question to a CSV file
        saveQuestionToCSV(question);
        return question;
    }

    // Method to save the question to a CSV file
    private void saveQuestionToCSV(String question) {
        String filePath = "questions.csv"; // File path to save questions

        try (FileWriter fileWriter = new FileWriter(filePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Writing question to CSV file. Assuming each question is written in a new row.
            printWriter.println(question);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/