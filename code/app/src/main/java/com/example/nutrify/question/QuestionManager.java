package com.example.nutrify.question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

public class QuestionManager extends QuestionManagement {

    // Method to ask a question and save both question and answer to a text file
    public String askUserQuestion(String question, String answer) {
        // Generate a new UUID for the question-answer pair and save it
        saveQuestionToTextFile(question, answer);
        return question;
    }

    // Private method to save a question and its answer to a text file
    private void saveQuestionToTextFile(String question, String answer) {
        String filePath = "questions.txt"; // File path to save questions and answers

        // Generate a new UUID for each question-answer pair
        UUID questionId = UUID.randomUUID();

        try (FileWriter fileWriter = new FileWriter(filePath, true); // 'true' for appending data
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Writing the UUID, question, and answer to the text file.
            // Format: "UUID ~ question ~ answer"
            printWriter.println(questionId.toString() + " ~ " + question + " ~ " + answer); // Save the question-answer pair

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve search history
    public ArrayList<String> searchHistory(String search) {
        ArrayList<String> questions = new ArrayList<>();
        String filePath = "questions.txt"; // Reading from the correct file (questions.txt)

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split("~");

                // Assuming that each line follows the format: "UUID ~ question ~ answer"
                if (columns.length >= 3) {
                    String question = columns[1].trim();
                    String answer = columns[2].trim();
                    if (question.toLowerCase().contains(search.toLowerCase()) || answer.toLowerCase().contains(search.toLowerCase())) {
                        questions.add(line); // Add the full question-answer pair to the list
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Method to delete question history
    public void deleteHistory(String questionId) {
        System.out.println("Deleting question with ID: " + questionId);
        // Logic to delete a question from the database (implementation not provided yet)
    }

    // Method to display a page with questions and answers
    public void displayPage() {
        System.out.println("Displaying the questions page...");
        // Logic to show question-related content (implementation not provided yet)
    }
}
