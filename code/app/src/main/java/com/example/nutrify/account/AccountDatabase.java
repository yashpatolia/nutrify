package com.example.nutrify.account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.UUID;

public class AccountDatabase {
    private final String filePath = "./src/main/java/com/example/nutrify/account/account.csv";
    public AccountDatabase(){

    }

    private String retrieve(String attribute, int column){

        BufferedReader reader = null;
        String line = "";
        try{
            reader = new BufferedReader(new FileReader(filePath));
            while((line = reader.readLine()) != null){

                String[] row = line.split(",");
                System.out.println(row.length + " " + Arrays.toString(row));
                if(row.length>column){
                    if(row[column].equals(attribute)){
                        return line;
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String retrieveExceptEdit(UUID userID){

        BufferedReader reader = null;
        String line = "";
        String data = "";
        try{
            reader = new BufferedReader(new FileReader(filePath));
            while((line = reader.readLine()) != null){
                String[] row = line.split(",");
                if(!row[0].equals(userID.toString())){
                    data += line+ "\n" ;
                }
            }
            return data;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void writeToDB(String content, boolean append) {
        try (FileWriter fileWriter = new FileWriter(filePath, append)) {
            fileWriter.write(content);
        } catch (Exception e) {
            System.out.println("Error has occurred writing to DB");
            throw new RuntimeException(e);
        }
    }

    public boolean createAccount(UserInformation userInfo){
        String infoForCSV = "\n";
        UUID userID = userInfo.getUserID();
        infoForCSV += userID.toString() + "," +
                userInfo.getUserUsername() + "," +
                userInfo.getUserEmail() + "," +
                userInfo.getUserPassword() + "," +
                userInfo.getUserPhoneNumber();
        try{
            writeToDB(infoForCSV, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editAccount(UserInformation userInfo){
        String content = retrieveExceptEdit(userInfo.getUserID());
        UUID userID = userInfo.getUserID();
        content += "\n" + userID.toString() + "," +
                userInfo.getUserUsername() + "," +
                userInfo.getUserPassword() + "," +
                userInfo.getUserEmail() + "," +
                userInfo.getUserPhoneNumber();

        try{
            writeToDB(content,false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserInfo(UUID userID){
        return retrieve(userID.toString(), 0);
    }

    public boolean properCredentials(String username, String password){

        String data = retrieve(username, 1);
        System.out.println(data);
        if(data != null){
            String[] attributes = data.split(",");
            return attributes[2].equals(password);
        }
        return false;
    }
}
