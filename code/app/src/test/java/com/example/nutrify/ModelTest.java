package com.example.nutrify;

import com.example.nutrify.expert.Expert;
import com.example.nutrify.expert.Model;

import org.junit.Assert;
import org.junit.Test;

public class ModelTest {

    private final Expert expert = new Model();

    @Test
    public void predictionTest(){
        String result = expert.getExpertAnswer("252,235,8,11,10,0,26,");
        String expected = "Cocoa";
        Assert.assertEquals(expected, result);
    }
}
