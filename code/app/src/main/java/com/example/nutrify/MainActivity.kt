package com.example.nutrify
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.example.nutrify.account.AccountDatabase

import com.example.nutrify.account.AccountManager




class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MyTag", "This is an info message")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("File",
        this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?.let { Log.i("Files", it.absolutePath) }.toString());

        val accountManagement = AccountManager("/storage/emulated/0/Android/data/com.example.nutrify/files/Download/account.csv");
        var usernameInput : EditText = findViewById(R.id.username_input)
        var passwordInput : EditText =findViewById(R.id.password_input)

        var loginButton : Button = findViewById(R.id.login_but)
        var createButton : Button = findViewById(R.id.create_but)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            if (accountManagement.login(username, password)) {
                Log.i("Login Attempt", "Successful")
            }
            else{
                Log.i("Login Attempt", "Failure")
            }
        }

        createButton.setOnClickListener{
            setContentView(R.layout.create_account)
            var usernameInput : EditText = findViewById(R.id.username_input)
            var passwordInput : EditText =findViewById(R.id.password_input)
            var createButton : Button = findViewById(R.id.create_but)
            var passwordConfInput : EditText =findViewById(R.id.password_conf_input)
            var emailInput : EditText =findViewById(R.id.email_input)
            var phoneInput : EditText =findViewById(R.id.phone_input)


            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val passwordConf = passwordConfInput.text.toString()
            val email = emailInput.text.toString()
            val phone = phoneInput.text.toString()

            createButton.setOnClickListener{
                if(passwordConf == password){
                    Log.i("Account", "Creating Account")
                    accountManagement.createAccount(username, password, email, phone)

                }
            }
        }

    }

}
