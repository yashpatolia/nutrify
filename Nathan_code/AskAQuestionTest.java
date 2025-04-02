import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AskAQuestionTest {
    public static void main(String[] args) {
        // Create an instance of QuestionManager (no database interaction needed)
        QuestionManager manager = new QuestionManager();

        // Test asking a question and saving it to the CSV file
        System.out.println("Testing askUserQuestion:");

        String question = "Hello?";
        manager.askUserQuestion(question);

        // Verify if the question was saved to the CSV file
        verifyQuestionSavedToCSV(question);

        System.out.println("Test completed!");
    }

    // Helper method to verify if the question is saved in the CSV file
    private static void verifyQuestionSavedToCSV(String expectedQuestion) {
        String filePath = "questions.csv";
        
        try {
            // Read all lines from the CSV file
            File file = new File(filePath);
            if (file.exists()) {
                // Read all lines from the CSV file
                var lines = Files.readAllLines(Paths.get(filePath));
                
                // Check if the last line matches the expected question
                if (!lines.isEmpty()) {
                    String lastLine = lines.get(lines.size() - 1);
                    if (lastLine.equals(expectedQuestion)) {
                        System.out.println("Test passed! Question saved to CSV.");
                    } else {
                        System.out.println("Test failed! Last saved question does not match.");
                    }
                } else {
                    System.out.println("Test failed! CSV file is empty.");
                }
            } else {
                System.out.println("Test failed! CSV file does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the CSV file.");
        }
    }
}
