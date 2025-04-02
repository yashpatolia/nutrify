package com.example.nutrify.ui.chatpage

data class Message(
    var text: String?=null,
    var sender: String,
    var isUser: Boolean = false
)

val messageDummy = listOf(
    Message(
        text = "hi",
        sender =  "user",
        isUser = true
    ),
    Message(
        text = "hello",
        sender = "bot",
        isUser = false
    )
)