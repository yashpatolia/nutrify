package com.example.nutrify
import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.nutrify.account.AccountManagement

import com.example.nutrify.account.AccountManager
import com.example.nutrify.ui.AdapterClass
import com.example.nutrify.expert.Expert
import com.example.nutrify.expert.Model
import com.example.nutrify.question.QuestionManagement
import java.util.UUID
import androidx.core.graphics.toColorInt
import com.example.nutrify.expert.LLMRecommender
import com.example.nutrify.expert.MacroMatcher
import com.example.nutrify.question.QuestionManager
import com.example.nutrify.ui.QuestionAnswer
import com.example.nutrify.ui.QuestionDetail

class MainActivity : ComponentActivity() {
    private lateinit var accountManagement: AccountManagement

    private lateinit var expert : Expert

    private lateinit var questionManagement: QuestionManagement

    private lateinit var userID : UUID

    private var currentUsername : String = ""

    private lateinit var recyclerView: RecyclerView

    private lateinit var searchRecyclerView: RecyclerView

    private val messageQueue: ArrayDeque<TextView> = ArrayDeque()

    private val attributes: ArrayDeque<EditText> = ArrayDeque()

    private lateinit var questionList : ArrayList<QuestionAnswer>

    private lateinit var searchResults : ArrayList<QuestionAnswer>

    //private lateinit var adapter : AdapterClass

    private lateinit var questionAnswer : ArrayList<ArrayList<String>>

    private lateinit var search : ArrayList<ArrayList<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeLogging()
        accountManagement = AccountManager("/storage/emulated/0/Android/data/com.example.nutrify/files/Download/account.csv")
        questionManagement = QuestionManager()
        questionList = arrayListOf<QuestionAnswer>()
        searchResults = arrayListOf<QuestionAnswer>()
        questionAnswer = arrayListOf(arrayListOf("question1", "question2"), arrayListOf("answer1", "answer2"), arrayListOf("0", "1"))
        search = arrayListOf(arrayListOf("question1"), arrayListOf("answer1"), arrayListOf("0"))

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
            handleLoginFail()

        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleLoginFail(){
        val textView = TextView(this)
        textView.text = "Incorrect username or password"
        textView.width = 100
        textView.textSize = 18f
        textView.setHintTextColor(ContextCompat.getColor(this, R.color.white))
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.gravity = Gravity.CENTER
        findViewById<LinearLayout>(R.id.login_fail).addView(textView)
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
        val expert : Expert = LLMRecommender()
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
            val userInput = questionInput.text.toString()
            Log.i("LLM_INPUT", "Prompt = $userInput") //Check whats being passed to the llm
            addMessageBubble(questionInput.text.toString(), true, questionContainer)
            var result : String = expert.getExpertAnswer(questionInput.text.toString())
            questionContainer.postDelayed({ addMessageBubble(result, false, questionContainer) }, 1000)
            questionInput.text.clear()
        }

        profileBut.setOnClickListener {
            setUpEdit(false)
        }

        historyBut.setOnClickListener {
            setContentView(R.layout.question_history)
            setUpQuestionHistory()
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


        getQuestionHistory()

        backButton.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }

        searchButton.setOnClickListener {
            setContentView(R.layout.search)
            handleQuestionSearch()
        }

    }

    private fun getQuestionHistory() {
        //get list of questions
        questionList.clear()
        //val questionAnswer = questionManagement.searchHistory(" ") // make a method to return all questions?
        for (i in questionAnswer[0].indices) {
            val questionAnswerObject = QuestionAnswer(questionAnswer[0][i], questionAnswer[1][i], questionAnswer[2][i])
            questionList.add(questionAnswerObject)
        }

        var adapter = AdapterClass(questionList, questionManagement)
        recyclerView.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, QuestionDetail::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }

    }


    private fun setUpQuestionSearch(searchValue: String) {
        searchRecyclerView = findViewById(R.id.questionlist)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.setHasFixedSize(true)
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
        searchResults.clear()
        //val search = questionManagement.searchHistory(searchValue)

        for (i in search[0].indices) {
            val questionAnswerObject = QuestionAnswer(search[0][i], search[1][i], search[2][i])
            searchResults.add(questionAnswerObject)
        }

        var adapter = AdapterClass(searchResults, questionManagement)
        searchRecyclerView.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, QuestionDetail::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }
    }


    private fun setUpEdit(editable: Boolean){
        if(editable){
            setContentView((R.layout.editing_account))
        }
        else{
            setContentView(R.layout.edit_account)
        }

        val userContainer : LinearLayout = findViewById(R.id.user_container)
        val passContainer : LinearLayout = findViewById(R.id.pass_container)
        val emailContainer : LinearLayout = findViewById(R.id.email_container)
        val phoneContainer : LinearLayout = findViewById(R.id.phone_container)
        val backArrow: ImageView = findViewById(R.id.back_arrow)

        val info : String = accountManagement.getAccount(userID)
        val infoSep : List<String> = info.split(",")

        addAccountInfo(infoSep[1], userContainer, editable, 0)
        addAccountInfo(infoSep[2], passContainer, editable, 1)
        addAccountInfo(infoSep[3], emailContainer, editable, 2)
        addAccountInfo(infoSep[4], phoneContainer, editable, 3)

        backArrow.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }

        if(editable){
            val saveBut : Button = findViewById(R.id.save_but)
            val newUsernameText : EditText = attributes.removeFirst()
            val newPasswordText : EditText = attributes.removeFirst()
            val newEmailText : EditText = attributes.removeFirst()
            val newPhoneText : EditText = attributes.removeFirst()


            saveBut.setOnClickListener {
                val newUsername : String = if(newUsernameText.text.toString() == ""){
                    newUsernameText.hint.toString()
                } else{
                    newUsernameText.text.toString()
                }
                val newPassword : String = if(newPasswordText.text.toString() == ""){
                    newPasswordText.hint.toString()
                } else{
                    newPasswordText.text.toString()
                }
                Log.i("password", newPasswordText.text.toString())
                val newEmail : String = if(newEmailText.text.toString() == ""){
                    newEmailText.hint.toString()
                } else{
                    newEmailText.text.toString()
                }
                val newPhone : String = if(newPhoneText.text.toString() == ""){
                    newPhoneText.hint.toString()
                } else{
                    newPhoneText.text.toString()
                }

                Log.i("Account", "$newUsername $newPassword $newEmail $newPhone")
                if(accountManagement.editAccount(userID, newUsername, newPassword, newEmail, newPhone)){
                    Log.i("Account", "Update successful")
                    setUpEdit(false)


                }else{
                    Log.i("Account", "Update failed")
                }

            }
        }
        else{
            val editBut : Button = findViewById(R.id.edit)
            editBut.setOnClickListener {
                setUpEdit(true)
            }
        }

    }


    private fun addAccountInfo(info : String, container : LinearLayout, editable : Boolean, attribute : Int){
        if(editable){
            val editTextView = EditText(this)
            editTextView.hint = info
            editTextView.width = 650
            editTextView.textSize = 20f
            editTextView.setHintTextColor(ContextCompat.getColor(this, R.color.white))
            editTextView.setTextColor(ContextCompat.getColor(this, R.color.white))
            editTextView.background = ContextCompat.getDrawable(this, R.drawable.rounded_corner)
            editTextView.setBackgroundColor("#4c4c4c".toColorInt())
            editTextView.setPadding(15)
            attributes.add(editTextView)
            container.addView(editTextView)
        }
        else{
            val textView = TextView(this)
            textView.text = info
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
            textView.textSize = 20f
            textView.setPadding(15)
            container.addView(textView)
        }

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
            setUpResponse(result)
        }

        backArrow.setOnClickListener {
            setContentView(R.layout.home)
            setupQuestionPrompt()
        }
    }


    private fun handleMacros(grams : String, calories : String, protein : String, fat : String, sat : String, fibre : String, carb : String) : String{

        if(sat.toInt() + fibre.toInt() >= 40){
            expert = Model()
            val questionCS : String = "$grams,$calories,$protein,$fat,$sat,$fibre,$carb,"
            return expert.getExpertAnswer(questionCS)
        }
        else{
            expert = MacroMatcher()
            val questionCS : String = "$calories,$protein,$carb,$fat,"
            return expert.getExpertAnswer(questionCS)
        }

    }


    private fun setUpResponse(result : String){
        setContentView(R.layout.answer)

        val backBut : ImageView = findViewById(R.id.back_arrow)
        val againBut : Button = findViewById(R.id.ask_again)

        handleResponse(result, findViewById(R.id.response))

        backBut.setOnClickListener {
            setContentView(R.layout.macros)
            setUpMacroView()
        }

        againBut.setOnClickListener {
            setContentView(R.layout.answer)
            handleResponse(result, findViewById(R.id.response))
        }

    }


    private fun handleResponse(food: String, layout: LinearLayout){
        val textView = TextView(this)
        Log.i("Handle Response", food)
        textView.text = food
        textView.setTextColor(resources.getColor(R.color.white))
        textView.textSize = 30f
        textView.setPadding(15)
        textView.gravity = Gravity.CENTER

        layout.addView(textView)
    }
}
