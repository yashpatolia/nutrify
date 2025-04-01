package com.example.nutrify.account;

import java.util.UUID;

public abstract class AccountManagement {
    private final AccountDatabase accountDB;

    public AccountManagement(){
        accountDB = new AccountDatabase();
    }

    public abstract UUID createAccount(String username, String password, String email, String phoneNumber);

    public abstract boolean editAccount(UUID userID, String username, String password, String email, String phoneNumber);

    public String getAccount(UUID userID){
        return accountDB.getUserInfo(userID);
    }
}
