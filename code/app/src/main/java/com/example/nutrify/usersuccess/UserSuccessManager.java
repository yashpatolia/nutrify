package com.example.nutrify.usersuccess;

public class UserSuccessManager extends UserSuccessManagement {
    //private final AccountDatabase accountDB;

    //public UserSuccessManager(String filePath) {
        //accountDB = new AccountDatabase(filePath);
    //}

    @Override
    public String getRatingMessage() {
        return "Give us a rating from 1 to 5 stars!";
    }

    @Override
    public void rateAnswer(int rating) {
        System.out.println("System given a rating of " + rating + " stars.");
    }

}
