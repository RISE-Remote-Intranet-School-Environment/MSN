
package org.schoolapp.project

//import DocsTab
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@Composable
fun ProfessorDetailsScreen(professor: Professor, onNavigateBack: () -> Unit) {
    var showConversation by remember { mutableStateOf(false) }

    if (showConversation) {
        ConversationScreen(professor = professor, onNavigateBack = { showConversation = false })
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(professor.name) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Professors"
                            )
                        }
                    }
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Professor Details",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Name: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.name)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Email: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.email)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Course: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.course)

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { showConversation = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Contact Professor")
                            }
                        }
                    }
                }
            }
        )
    }
}
