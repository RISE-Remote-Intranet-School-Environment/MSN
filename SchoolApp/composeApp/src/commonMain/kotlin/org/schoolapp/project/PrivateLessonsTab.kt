package org.schoolapp.project

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star

data class Tutor(val name: String, val rating: Int)

@Composable
fun PrivateLessonsTab() {
    val tutors = listOf(
        Tutor("Marie", 5),
        Tutor("Ayoub", 4),
        Tutor("Lina", 4)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(tutors) { tutor ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = tutor.name, style = MaterialTheme.typography.h6)
                    Row {
                        repeat(tutor.rating) {
                            Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                        }
                    }
                }
                Divider()
            }
        }

        TextField(
            value = "",
            onValueChange = { /* Handle input */ },
            placeholder = { Text("Type here...") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}
