package org.schoolapp.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons


@Composable
fun ClassesView(
    onNavigateBack: () -> Unit,
    onNavigateToProfessors: () -> Unit,
    onNavigateToMyCourses: () -> Unit,
    enrolledCourses: MutableList<String>,
    onEnroll: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val availableCourses = CourseRepository.courses
    val filteredCourses = availableCourses.filter { it.name.contains(searchQuery.text, ignoreCase = true) }
    val buttonStates = remember { mutableStateMapOf<String, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text("Search courses") }
                )

                // Courses List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredCourses) { course ->
                        ClassItem(
                            course = course,
                            isRegistered = buttonStates[course.name] ?: false,
                            onEnroll = {
                                buttonStates[course.name] = true
                                onEnroll(course.name)
                            }
                        )
                    }
                }

                // Navigation Buttons
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onNavigateToProfessors) {
                        Text("Professors")
                    }
                    Button(onClick = onNavigateToMyCourses) {
                        Text("My Courses")
                    }
                }
            }
        }
    )
}

@Composable
fun ClassItem(course: Course, isRegistered: Boolean, onEnroll: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ok),
                    contentDescription = "Course Image",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                )
                Column {
                    Text(text = course.name, style = MaterialTheme.typography.h6)
                    Text(text = course.teacher, style = MaterialTheme.typography.body2)
                }
            }

            Button(
                onClick = onEnroll,
                enabled = !isRegistered
            ) {
                Text(if (isRegistered) "Registered" else "Register")
            }
        }
    }
}
