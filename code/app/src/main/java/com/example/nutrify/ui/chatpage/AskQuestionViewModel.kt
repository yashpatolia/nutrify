package com.example.nutrify.ui.chatpage

import com.example.nutrify.expert.Expert
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {
    private val messageList: MutableList<Message> = mutableStateListOf(*messageDummy.toTypedArray())
    val messages: List<Message> = messageList

    fun addMessage(message: Message) {
        messageList.add(0, message)
    }

    fun sendQuestionToExperts(question: Message) {
        addMessage(question)
        viewModelScope.launch {
            val expert = Expert(
                function = TODO()
            )

        }
    }
}