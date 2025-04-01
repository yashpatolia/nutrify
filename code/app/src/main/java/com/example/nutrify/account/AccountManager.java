package com.example.nutrify.account;

import java.util.UUID;

public class AccountManager extends AccountManagement{

    private final AccountDatabase accountDB;

    public AccountManager(){
        accountDB = new AccountDatabase();
    }
    public UUID createAccount(String username, String password, String email, String phoneNumber){
        UserInformation newUser = new UserInformation(username, email, password, phoneNumber);
        accountDB.createAccount(newUser);
        return newUser.getUserID();
    }

    public boolean editAccount(UUID userID, String username, String password, String email, String phoneNumber){
        UserInformation updatedUser = new UserInformation(userID, username, email, password, phoneNumber);
        return accountDB.editAccount(updatedUser);
    }
}
