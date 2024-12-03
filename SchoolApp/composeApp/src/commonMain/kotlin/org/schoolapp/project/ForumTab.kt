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
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

data class Message(val sender: String, val timestamp: String, val content: String)

@Composable
fun ForumTab() {
    var newMessage by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            Message("Alice", "2024-11-21 10:00", "Hello everyone!"),
            Message("Bob", "2024-11-21 10:15", "Can someone help me with calculus?")
        )
    }

    val groupedMessages = messages.groupBy { it.timestamp.split(" ")[0] }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val fileName = uri.lastPathSegment ?: "File"
            messages.add(
                Message(
                    sender = "You",
                    timestamp = java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date()),
                    content = "📎 File: $fileName"
                )
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedMessages.forEach { (date, messages) ->
                item {
                    Text(
                        text = date, // Display the date separator
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.subtitle2,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
                items(messages) { message ->
                    ChatBubble(message = message)
                }
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
            IconButton(onClick = { filePickerLauncher.launch("*/*") }) { // Allow all file types
                Text("📎")
            }

            EmojiPicker(messages)

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
                        val timestamp = java.text.SimpleDateFormat(
                            "yyyy-MM-dd HH:mm",
                            java.util.Locale.getDefault()
                        ).format(java.util.Date())

                        messages.add(
                            Message(
                                sender = "You", // Change to actual sender
                                timestamp = timestamp,
                                content = newMessage
                            )
                        )
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
fun EmojiPicker(messages: MutableList<Message>) {
    var expanded by remember { mutableStateOf(false) }
    val emojiList = listOf("😊", "😂", "👍", "❤️", "🎉", "🔥")

    Box {
        IconButton(onClick = { expanded = true }) {
            Text("😊")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            emojiList.forEach { emoji ->
                DropdownMenuItem(onClick = {
                    val timestamp = java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date())

                    messages.add(
                        Message(
                            sender = "You",
                            timestamp = timestamp,
                            content = emoji
                        )
                    )
                    expanded = false
                }) {
                    Text(emoji)
                }
            }
        }
    }
}


@Composable
fun ChatBubble(message: Message) {
    val isUserMessage = message.sender == "You"

    // Define colors for user and non-user messages
    val userMessageColor = Color(0xFF1E88E5)
    val nonUserMessageColor = Color.LightGray.copy(alpha = 0.2f)

    Column(
        horizontalAlignment = if (isUserMessage) Alignment.End else Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Display sender name and timestamp
        Text(
            text = if (isUserMessage) "You" else message.sender,
            style = MaterialTheme.typography.subtitle2,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .wrapContentWidth(if (isUserMessage) Alignment.End else Alignment.Start)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isUserMessage) userMessageColor else nonUserMessageColor)
                .padding(8.dp)
        ) {
            Text(
                text = message.content,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                color = if (isUserMessage) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
            )
        }

        // Display timestamp
        Text(
            text = message.timestamp.split(" ")[1], // Show only the time
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
