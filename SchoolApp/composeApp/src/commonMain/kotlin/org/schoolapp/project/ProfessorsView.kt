package org.schoolapp.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

@Composable
fun ProfessorsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfessorDetails: (Professor) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Professors") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Classes"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(ProfessorRepository.professors) { professor ->
                    ProfessorItem(
                        professor = professor,
                        onClick = { onNavigateToProfessorDetails(professor) }
                    )
                }
            }
        }
    )
}

@Composable
fun ProfessorItem(professor: Professor, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = professor.image),
            contentDescription = professor.name,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = professor.name,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "Courses: ${professor.courseCount}",
                style = MaterialTheme.typography.body2
            )
        }

        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "More details"
            )
        }
    }
}
