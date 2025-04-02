package com.example.nutrify.account;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccountDatabase {
    private final String filePath;

    public AccountDatabase(String filePath){
        this.filePath = filePath;
    }

    private String retrieve(String attribute, int column){
    //    Log.i("Retrieve", "Retrieving " + attribute + " from " + filePath );
        BufferedReader reader = null;
        String line = "";
        try{

            reader = new BufferedReader(new FileReader(filePath));

            while((line = reader.readLine()) != null){
                System.out.println(line);
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

    private List<String[]> retrieveAll(){
        BufferedReader reader = null;
        String line = "";
        List<String[]> data = new ArrayList<>();
        try{

            reader = new BufferedReader(new FileReader(filePath));

            while((line = reader.readLine()) != null){
                System.out.println(line);
                String[] row = line.split(",");
                if(row.length > 0){
                    data.add(row);
                }

            }
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                userInfo.getUserPassword() + "," +
                userInfo.getUserEmail() + "," +
                userInfo.getUserPhoneNumber();
        try{
            writeToDB(infoForCSV, true);
            return true;
        } catch (Exception e) {
           // Log.i("AccountDB", e.toString());
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

    public boolean usernameExists(String username){
        return retrieve(username, 1) != null;
    }

    public boolean properCredentials(String username, String password){
        System.out.println(username + " " + password);
        String data = retrieve(username, 1);
        System.out.println(data);
        if(data != null){
            String[] attributes = data.split(",");
            return attributes[2].equals(password);
        }
        return false;
    }

    public boolean delete(String username){
        List<String[]> entries = retrieveAll();
        try{
            for(String[] s : entries){
                if(s[0].equals(username)){
                    entries.remove(s);
                    break;
                }
            }
            for(int i = 0; i < entries.size(); i++){
                String content = "";
                if(i != 0){
                    content = "\n";
                }
                content += entries.get(i)[0]  + "," +
                    entries.get(i)[1] + "," +
                    entries.get(i)[2] + "," +
                    entries.get(i)[3] + "," +
                    entries.get(i)[4];
                writeToDB(content, i != 0);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
