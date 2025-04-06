package com.example.nutrify.question;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionManager extends QuestionManagement {

    // Method to ask a question and save both question and answer to a text file
    public String askUserQuestion(String question, String answer, UUID userID) {
        // Generate a new UUID for the question-answer pair and save it
        saveQuestionToTextFile(question, answer, userID);
        return question;
    }

    // Private method to save a question and its answer to a text file
    private void saveQuestionToTextFile(String question, String answer, UUID userID) {
        String filePath = "questions.txt"; // File path to save questions and answers

        // Generate a new UUID for each question-answer pair
        UUID questionId = UUID.randomUUID();

        try (FileWriter fileWriter = new FileWriter(filePath, true); // 'true' for appending data
                PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Writing the UUID, question, and answer to the text file.
            // Format: "UUID ~ question ~ answer"
            printWriter.println(questionId.toString() + " ~ " + question + " ~ " + answer + " ~ " + userID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve search history
    public List<List<String>> searchHistory(UUID userID) {
        String filePath = "questions.txt"; // Reading from the correct file (questions.txt)

        List<String> questionID = new ArrayList<>();
        List<String> question = new ArrayList<>();
        List<String> answer = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split("~");

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Assuming that each line follows the format: "UUID ~ question ~ answer"
                if (columns.length >= 4) {
                    if (!columns[3].equals(userID.toString())) {
                        continue; // Skip if the userID does not match
                    }
                    questionID.add(columns[0]);
                    question.add(columns[1]);
                    answer.add(columns[2]);
                }
            }

            List<List<String>> questionAnswer = new ArrayList<>();
            questionAnswer.add(questionID);
            questionAnswer.add(question);
            questionAnswer.add(answer);
            return questionAnswer;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method to delete question history
    public void deleteHistory(String questionId) {

        File questionHistory = new File("questions.txt"); // Reading from the text file with question history,
                                                          // questions.txt
        File newQuestionHistory = new File("updatedQuestions.txt"); // A new file where the updates question history
                                                                    // will be stored

        try (
                BufferedReader br = new BufferedReader(new FileReader(questionHistory));
                BufferedWriter bw = new BufferedWriter(new FileWriter(newQuestionHistory));) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] columns = line.split("~");
                if (columns.length >= 3) {

                    if (!columns[0].equals(questionId)) {
                        bw.write(line);
                        bw.newLine();
                    }

                }

            }

            if (!questionHistory.delete())
                throw new IOException("Failed to delete original question history file");

            if (!newQuestionHistory.renameTo(questionHistory))
                throw new IOException("Failed to rename new question history file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display a page with questions and answers
    public void displayPage() {
        System.out.println("Displaying the questions page...");
        // Logic to show question-related content (implementation not provided yet)
    }
}
