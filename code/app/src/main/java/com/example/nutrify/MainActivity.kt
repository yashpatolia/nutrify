package com.example.nutrify

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity

import com.example.nutrify.account.AccountManager

class MainActivity : ComponentActivity() {
    private lateinit var accountManagement: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeLogging()
        accountManagement = AccountManager("/storage/emulated/0/Android/data/com.example.nutrify/files/Download/account.csv")
        setupLoginUI()
    }

    private fun initializeLogging() {
        Log.i("MyTag", "This is an info message")
        val downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        downloadsDir?.let { Log.i("Files", it.absolutePath) }
    }

    private fun setupLoginUI() {
        val usernameInput: EditText = findViewById(R.id.username_input)
        val passwordInput: EditText = findViewById(R.id.password_input)
        val loginButton: Button = findViewById(R.id.login_but)
        val createButton: Button = findViewById(R.id.create_but)

        loginButton.setOnClickListener {
            handleLogin(usernameInput.text.toString(), passwordInput.text.toString())
        }

        createButton.setOnClickListener {
            showCreateAccountUI()
        }
    }

    private fun handleLogin(username: String, password: String) {
        if (accountManagement.login(username, password)) {
            Log.i("Login Attempt", "Successful")
        } else {
            Log.i("Login Attempt", "Failure")
        }
    }

    private fun showCreateAccountUI() {
        setContentView(R.layout.create_account)
        setupCreateAccountUI()
    }

    private fun setupCreateAccountUI() {
        val creUsernameInput: EditText = findViewById(R.id.cre_username_input)
        val crePasswordInput: EditText = findViewById(R.id.cre_password_input)
        val passwordConfInput: EditText = findViewById(R.id.password_conf_input)
        val emailInput: EditText = findViewById(R.id.email_input)
        val phoneInput: EditText = findViewById(R.id.phone_input)
        val createButton: Button = findViewById(R.id.cre_create_but)
        val backArrow: ImageView = findViewById(R.id.back_arrow);

        createButton.setOnClickListener {
            handleCreateAccount(
                creUsernameInput.text.toString(),
                crePasswordInput.text.toString(),
                passwordConfInput.text.toString(),
                emailInput.text.toString(),
                phoneInput.text.toString()
            )
        }

        backArrow.setOnClickListener {
            setContentView(R.layout.activity_main)
            setupLoginUI()
        }


    }

    private fun handleCreateAccount(username: String, password: String, passwordConf: String, email: String, phone: String) {
        if (passwordConf == password) {
            Log.i("Account", "Creating Account: $username $password")
            accountManagement.createAccount(username, password, email, phone)
            setContentView(R.layout.activity_main)
            setupLoginUI()
        } else {
            Log.i("Account", "Password confirmation does not match")
        }
    }
}