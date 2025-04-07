package com.example.nutrify;

import static org.junit.Assert.assertEquals;

import com.example.nutrify.question.QuestionManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionManagerTest {
    private final QuestionManager questionManager = new QuestionManager();

    @Test
    public void searchQuestionTest() {
        List<List<String>> expectedResult = new ArrayList<>();
        List<List<String>> actualResult = new ArrayList<>();
        List<String> questionList = new ArrayList<>();
        List<String> answerList = new ArrayList<>();

        questionList.add("question example");
        answerList.add("answer example");
        expectedResult.add(questionList);
        expectedResult.add(answerList);
        UUID userID = UUID.randomUUID();

        questionManager.askUserQuestion("question example", "answer example", userID);
        List<List<String>> questionHistory = questionManager.searchHistory(userID);
        actualResult.add(questionHistory.get(1));
        actualResult.add(questionHistory.get(2));

        assertEquals(expectedResult, actualResult);

    }



}
