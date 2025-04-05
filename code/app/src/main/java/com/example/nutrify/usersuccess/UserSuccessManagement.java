package com.example.nutrify.usersuccess;

import com.example.nutrify.account.AccountDatabase;

public abstract class UserSuccessManagement {
    //private final AccountDatabase accountDB;

    //public AccountManagement(String filePath){
        //accountDB = new AccountDatabase(filePath);
    //}

    public abstract String getRatingMessage();

    public abstract void rateAnswer(int rating);

}
