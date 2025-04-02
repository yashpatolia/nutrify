import java.util.ArrayList;

public abstract class QuestionManagement {
    // // Declare field for the database service
    // protected QuestionDatabase questionDB;

    // // Constructor to initialize the database
    // public QuestionManagement(QuestionDatabase questionDB) {
    // this.questionDB = questionDB;
    // }

    // Abstract method for searching history
    public abstract ArrayList<String> searchHistory();

    // Abstract method for displaying page
    public abstract void displayPage();

    // Abstract method for asking a question
    public abstract String askUserQuestion(String question);

    // Abstract method for deleting question history
    public abstract void deleteHistory(String questionId);
}
