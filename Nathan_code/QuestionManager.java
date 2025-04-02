import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class QuestionManager extends QuestionManagement {

    // private QuestionDatabase questionDB; // Database for storing questions

    // // Constructor to initialize the database
    // public QuestionManager(QuestionDatabase questionDB) {
    // this.questionDB = questionDB;
    // }

    // Method to ask a question and save it to a CSV file
    public String askUserQuestion(String question) {
        saveQuestionToCSV(question);
        return question;
    }

    // Private method to save a question to a CSV file
    private void saveQuestionToCSV(String question) {
        String filePath = "questions.csv"; // File path to save questions

        try (FileWriter fileWriter = new FileWriter(filePath, true);
                PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Writing question to CSV file. Assuming each question is written in a new row.
            printWriter.println(question);
            System.out.println("Question saved: " + question);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve search history
    public ArrayList<String> searchHistory(String search) {
        // Logic to retrieve and display search history from the database
        ArrayList<String> questions = new ArrayList<>();
        String filePath = "questions.csv";
        try (FileReader fileReader = new FileReader(filePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.toLowerCase().contains(search.toLowerCase())) {
                    questions.add(line);
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
        // Logic to delete a question from the database
    }

    // Method to display a page with questions
    public void displayPage() {
        System.out.println("Displaying the questions page...");
        // Logic to show question-related content
    }
}
