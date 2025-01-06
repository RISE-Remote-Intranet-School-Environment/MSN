package org.schoolapp.project

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*


data class Message(val sender: String, val timestamp: String, val content: String)

val messages = mutableListOf(
    Message("Alice", "2024-11-21 10:00", "Hello everyone!"),
    Message("Bob", "2024-11-21 10:15", "Can someone help me with calculus?")
)

fun getCurrentTimestamp(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
}

fun addFileMessage(uri: Uri?, messages: MutableList<Message>) {
    uri?.let {
        val fileName = uri.lastPathSegment ?: "File"
        messages.add(
            Message(
                sender = "You",
                timestamp = getCurrentTimestamp(),
                content = "📎 File: $fileName"
            )
        )
    }
}
