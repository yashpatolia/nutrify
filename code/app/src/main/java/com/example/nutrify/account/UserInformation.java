package com.example.nutrify.account;

import java.util.UUID;

public class UserInformation {
    private String username;
    private String password;
    public UUID userID; // auto generated id
    public String email;
    public String phoneNumber;

    public UserInformation(String username, String email, String password, String phoneNumber){
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userID = UUID.randomUUID();
    }

    public UserInformation(UUID userID, String username, String email, String password, String phoneNumber){
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userID = userID;
    }

    public void setUserUsername(String username){
        this.username = username;
    }
    public void setUserPassword(String password){
        this.password = password;
    }
    public void setUserEmail(String email){
        this.email = email;
    }
    public void setUserPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public UUID getUserID(){
        return this.userID;
    }
    public String getUserUsername(){
        return this.username;
    }
    public String getUserPassword(){
        return this.password;
    }
    public String getUserEmail(){
        return this.email;
    }
    public String getUserPhoneNumber(){
        return this.phoneNumber;
    }



}
