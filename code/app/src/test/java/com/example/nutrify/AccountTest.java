package com.example.nutrify;

import com.example.nutrify.account.AccountManager;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class AccountTest {
    private final AccountManager accountManagement = new AccountManager();

    @Test
    public void createAccountTest(){
        UUID userID = accountManagement.createAccount("Tuname", "Tpass", "Temail", "Tpn");
        String expected = userID.toString()+",Tuname,Temail,Tpass,Tpn";
        Assert.assertEquals(expected, accountManagement.getAccount(userID));
    }

    @Test
    public void editAccount(){
        UUID userID = accountManagement.createAccount("Tuname", "Tpass", "Temail", "Tpn");
        String expected = userID.toString()+",Tuname,Temail,Tpass,Tpn";
        Assert.assertEquals(expected, accountManagement.getAccount(userID));
        accountManagement.editAccount(userID, "UpU", "Uppass", "Uemail", "Upn");
        expected = userID.toString()+",UpU,Uppass,Uemail,Upn";
        Assert.assertEquals(expected, accountManagement.getAccount(userID));
    }

    @Test
    public void testLoginSuccess(){
        boolean expected = true;
        Assert.assertEquals(expected, accountManagement.login("Justin", "mypass"));
    }

    @Test
    public void testLoginFail(){
        boolean expected = false;
        Assert.assertEquals(expected, accountManagement.login("Justin", "wrongpass"));
    }
}
