package com.example.nutrify.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrify.R

class QuestionDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.past_question)

        val getData = intent.getParcelableExtra<QuestionAnswer>("android")
        if (getData != null) {
            val pastQuestion : TextView = findViewById(R.id.outgoingHistory)
            val pastAnswer : TextView = findViewById(R.id.incomingHistory)

            pastQuestion.text = getData.question
            pastAnswer.text = getData.answer
        }
    }
}