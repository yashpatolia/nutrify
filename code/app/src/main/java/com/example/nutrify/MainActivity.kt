package com.example.nutrify

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.nutrify.account.AccountManager
import com.example.nutrify.ui.AdapterClass
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var accountManagement: AccountManager

    private lateinit var userID : UUID

    private var currentUsername : String = ""

    private lateinit var recyclerView: RecyclerView

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
            Log.i("Login Attempt", "Successful current username : $username")
            userID = accountManagement.getID(username)
            currentUsername = username
            showQuestionPrompt()

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
        val backArrow: ImageView = findViewById(R.id.back_arrow)

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

    private fun showQuestionPrompt(){
        setContentView(R.layout.home)
        setupQuestionPrompt()
    }

    private fun setupQuestionPrompt(){
        val questionInput : EditText = findViewById(R.id.question_prompt)
        val logoutButton : ImageView = findViewById(R.id.logout)
        val submitQuestion : ImageView = findViewById(R.id.sendButton)
        val questionContainer : LinearLayout = findViewById(R.id.messageContainer)

        logoutButton.setOnClickListener {
            setContentView(R.layout.activity_main)
            setupLoginUI()
        }

        submitQuestion.setOnClickListener {

            addMessageBubble(questionInput.text.toString(), true, questionContainer)
            questionInput.text.clear()
            handleQuestion(questionInput.toString())
            //send question to question class for response
            questionContainer.postDelayed({ addMessageBubble("Auto-response", false, questionContainer) }, 1000)
        }

    }

    private fun handleQuestion(question : String){
        if(question.isNotEmpty()){

        }

    }


    private fun addMessageBubble(text: String, isUser: Boolean, questionContainer : LinearLayout) {
        val textView = TextView(this)
        textView.text = text
        textView.setBackgroundResource(R.drawable.text_bubble_right)
        textView.setPadding(20, 10, 20, 10)
        textView.textSize = 16f

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            if (isUser) {
                setMargins(50, 10, 10, 10)
                textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
                gravity = Gravity.END
            } else {
                setMargins(10, 10, 50, 10)
                textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                gravity = Gravity.START
            }
        }

        textView.layoutParams = params

        textView.layoutParams = params

        questionContainer.addView(textView)
    }

    private fun setUpQuestionHistory() {
        recyclerView = findViewById(R.id.questionlist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val backButton : ImageView = findViewById(R.id.back_arrow_qhistory)
        val searchButton : ImageView = findViewById(R.id.search_button)


        getQuestionHistory()

        backButton.setOnClickListener {
            //setContentView(idk where we're accessing question history from LOL)

        }

        searchButton.setOnClickListener {
            setContentView(R.layout.search)
            handleQuestionSearch()
        }

    }

    private fun getQuestionHistory() {
        //get list of questions
        //val questionList
        //recyclerView.adapter = AdapterClass(questionList)
    }

    private fun setUpQuestionSearch(searchValue: String) {
        recyclerView = findViewById(R.id.questionlist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val backButton : ImageView = findViewById(R.id.back_arrow_qhistory)
        val searchButton : ImageView = findViewById(R.id.search_button)

        getSearch(searchValue)

        backButton.setOnClickListener {

            setUpQuestionHistory()

        }

        searchButton.setOnClickListener {
            setContentView(R.layout.search)
            handleQuestionSearch()
        }

    }

    private fun handleQuestionSearch(){
        val searchButton : ImageView = findViewById(R.id.search_button)
        val searchValue: EditText = findViewById(R.id.search_prompt)

        searchButton.setOnClickListener {
            setContentView(R.layout.question_history)
            setUpQuestionSearch(searchValue.text.toString())
        }

    }

    private fun getSearch(searchValue: String) {
        //get list of questions
        //val searchResults
        //recyclerView.adapter = AdapterClass(searchResults)
    }


}
