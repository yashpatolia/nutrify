package com.example.nutrify
import android.media.Image
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
import com.example.nutrify.account.AccountManagement

import com.example.nutrify.account.AccountManager
import com.example.nutrify.ui.AdapterClass
import com.example.nutrify.expert.Expert
import com.example.nutrify.expert.Model
import com.example.nutrify.question.QuestionManagement
import java.util.UUID

class MainActivity : ComponentActivity() {
    private lateinit var accountManagement: AccountManagement

    private lateinit var expert : Expert

    private lateinit var questionManagement: QuestionManagement

    private lateinit var userID : UUID

    private var currentUsername : String = ""

    private lateinit var recyclerView: RecyclerView

    private val messageQueue: ArrayDeque<TextView> = ArrayDeque();

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
        val macroBut : Button = findViewById(R.id.macro_but)
        val profileBut : ImageView = findViewById(R.id.rightImage)
        val historyBut : ImageView = findViewById(R.id.questionHistoryButton)

        macroBut.setOnClickListener {
            setContentView(R.layout.macros)
            setUpMacroView()
        }

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

        profileBut.setOnClickListener {
            setUpEdit()
        }

        historyBut.setOnClickListener {
            setContentView(R.layout.question_history)
            setUpQuestionHistory()
        }
    }

    private fun handleQuestion(question : String){
        if(question.isNotEmpty()){

        }

    }


    private fun addMessageBubble(text: String, isUser: Boolean, questionContainer : LinearLayout) {
        val textView = TextView(this)
        textView.text = text

        textView.id = messageQueue.size;
        if(messageQueue.size >= 10){
            questionContainer.removeView(messageQueue.removeFirst());
            questionContainer.removeView(messageQueue.removeFirst());
        }
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

        messageQueue.add(textView);

        questionContainer.addView(textView)
    }

    private fun setUpQuestionHistory() {
        recyclerView = findViewById(R.id.questionlist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val backButton : ImageView = findViewById(R.id.back_arrow_qhistory)
        val searchButton : ImageView = findViewById(R.id.search_button)
        val deleteButton : ImageView = findViewById(R.id.delete_button)


        getQuestionHistory()

        backButton.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }

        searchButton.setOnClickListener {
            setContentView(R.layout.search)
            handleQuestionSearch()
        }

        deleteButton.setOnClickListener {

        }

    }

    private fun getQuestionHistory() {
        //get list of questions

        //recyclerView.adapter = AdapterClass(questionList)
    }

    private fun handleDeleteButton() {

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
        val backButton: ImageView = findViewById(R.id.back_arrow_search)

        searchButton.setOnClickListener {
            setContentView(R.layout.question_history)
            setUpQuestionSearch(searchValue.text.toString())
        }

        backButton.setOnClickListener {
            setContentView(R.layout.question_history)
            setUpQuestionHistory()
        }

    }

    private fun getSearch(searchValue: String) {
        //get list of questions
        //recyclerView.adapter = AdapterClass(searchResults)
    }




    private fun setUpEdit(){
        setContentView(R.layout.edit_account)
        val userContainer : LinearLayout = findViewById(R.id.user_container)
        val passContainer : LinearLayout = findViewById(R.id.pass_container)
        val emailContainer : LinearLayout = findViewById(R.id.email_container)
        val phoneContainer : LinearLayout = findViewById(R.id.phone_container)
        val backArrow: ImageView = findViewById(R.id.back_arrow)
        val info : String = accountManagement.getAccount(userID)
        val infoSep : List<String> = info.split(",")

        addAccountInfo(infoSep[1], userContainer)
        addAccountInfo(infoSep[2], passContainer)
        addAccountInfo(infoSep[3], emailContainer)
        addAccountInfo(infoSep[4], phoneContainer)

        backArrow.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }
    }


    private fun addAccountInfo(info : String, container : LinearLayout){
        val textView = TextView(this)
        textView.text = info
        textView.setTextColor(resources.getColor(R. color. white))
        textView.textSize = 18f

        container.addView(textView)
    }

    private fun setUpMacroView(){

        val gramInput : EditText = findViewById(R.id.gram_input)
        val calorieInput : EditText = findViewById(R.id.calorie_input)
        val proteinInput : EditText = findViewById(R.id.protein_input)
        val fatInput : EditText = findViewById(R.id.fat_input)
        val satFatInput : EditText = findViewById(R.id.sat_input)
        val fibreInput : EditText = findViewById(R.id.fibre_input)
        val carbInput : EditText = findViewById(R.id.carbs_input)
        val getFoodBut : Button = findViewById(R.id.get_food)
        val backArrow: ImageView = findViewById(R.id.back_arrow)

        getFoodBut.setOnClickListener {
            val result : String = handleMacros(gramInput.text.toString(), calorieInput.text.toString(),
                proteinInput.text.toString(), fatInput.text.toString(),
                satFatInput.text.toString(), fibreInput.text.toString(), carbInput.text.toString())

            Log.i("Model", "result : $result")
        }

        backArrow.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }
    }

    private fun handleMacros(grams : String, calories : String, protein : String, fat : String, sat : String, fibre : String, carb : String) : String{
        expert = Model()
        val questionCS : String = "$grams,$calories,$protein,$fat,$sat,$fibre,$carb,"
        return expert.getExpertAnswer(questionCS)
    }
}
