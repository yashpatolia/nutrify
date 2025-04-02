package com.example.nutrify
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity


import com.example.nutrify.account.AccountManager

val accountManagement = AccountManager();


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var usernameInput : EditText = findViewById(R.id.username_input)
        var passwordInput : EditText =findViewById(R.id.password_input)
        var loginButton : Button = findViewById(R.id.login_but)
        var createButton : Button = findViewById(R.id.create_but)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString();
            val password = passwordInput.text.toString()
            if (accountManagement.login(username, password)) {
                //successful login route to home page
            }
        }

        createButton.setOnClickListener{
            
        }

    }

}
