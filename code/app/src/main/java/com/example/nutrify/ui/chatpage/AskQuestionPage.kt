package com.example.nutrify.ui.chatpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color

var message = mutableStateOf("")

@Composable
fun AskQuestionPage() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopSection()
        MessageSection()
        UserInput()
    }
}

@Composable
fun TopSection(title: String = "Nutrify") {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp).background(MaterialTheme.colorScheme.background)
    ) {
        Row (
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun MessageSection(modifier: Modifier = Modifier) {
    LazyColumn (
        modifier.fillMaxWidth().padding(16.dp).height(640.dp),
        reverseLayout = true
    ){
        items(messageDummy) { chat ->
            MessageBubble(
                message = chat.text,
                isUser = chat.isUser
            )
        }
    }
}

@Composable
fun MessageBubble(message: String?, isUser: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!message.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .background(
                        if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = message,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun UserInput() {
    Card(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
    ) {
        OutlinedTextField(
            placeholder = {Text("Enter nutrients")},
            value = message.value,
            onValueChange = { message.value = it},
            trailingIcon = {
                Icon(
                    Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "MessageButton",
                    modifier = Modifier.clickable{}
                )
            },
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        )
    }
}

@Preview
@Composable
fun AskQuestionPreview() {
    AskQuestionPage()
}