package com.example.nutrify.account;

import java.util.UUID;

public abstract class AccountManagement {
    private final AccountDatabase accountDB;

    public AccountManagement(String filePath){
        accountDB = new AccountDatabase(filePath);
    }

    public abstract UUID createAccount(String username, String password, String email, String phoneNumber);

    public abstract boolean editAccount(UUID userID, String username, String password, String email, String phoneNumber);

    public boolean login( String username, String password){
        return accountDB.properCredentials(username, password);
    }

    public abstract void viewAccount();

    public String getAccount(UUID userID){
        return accountDB.getUserInfo(userID);
    }
}
