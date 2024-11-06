package org.schoolapp.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForumTab() {
    var newMessage by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            "Hello everyone!",
            "Can someone help me with calculus?"
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }

        Divider(color = Color.Gray.copy(alpha = 0.2f))

        // Input area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO: Implement file upload */ }) {
                Text("📎") // Placeholder for file upload icon
            }

            IconButton(onClick = { /* TODO: Implement emoji picker */ }) {
                Text("😊") // Placeholder for emoji icon
            }

            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                placeholder = { Text("Message") },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray.copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = {
                    if (newMessage.isNotBlank()) {
                        messages.add("user: $newMessage")  // Prefix user messages
                        newMessage = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: String) {
    val isUserMessage = message.startsWith("user:")
    val displayMessage = if (isUserMessage) message.removePrefix("user:") else message

    // Define colors for user and non-user messages
    val userMessageColor = Color(0xFF1E88E5) //MaterialTheme.colors.primaryVariant
    val nonUserMessageColor = Color.LightGray.copy(alpha = 0.2f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(if (isUserMessage) Alignment.End else Alignment.Start)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isUserMessage) userMessageColor else nonUserMessageColor)
            .padding(8.dp)
    ) {
        Text(
            text = displayMessage,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            color = if (isUserMessage) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
        )
    }
}
