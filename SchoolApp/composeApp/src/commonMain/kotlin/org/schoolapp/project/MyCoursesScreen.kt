package org.schoolapp.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight



@Composable
fun MyCoursesScreen(
    enrolledCourses: List<String>,
    onNavigateBack: () -> Unit,
    onUnenroll: (String) -> Unit,
    onAccessDetails: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
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
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Enrolled Courses:",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (enrolledCourses.isEmpty()) {
                    Text("You are not enrolled in any courses yet.", style = MaterialTheme.typography.body1)
                } else {
                    LazyColumn {
                        items(enrolledCourses) { course ->
                            CourseItem(
                                courseName = course,
                                onUnenroll = { onUnenroll(course) },
                                onAccessDetails = { onAccessDetails(course) }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CourseItem(courseName: String, onUnenroll: () -> Unit, onAccessDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
            )
            Row {
                IconButton(onClick = onAccessDetails) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Access details of $courseName",
                        tint = MaterialTheme.colors.primary
                    )
                }
                IconButton(onClick = onUnenroll) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Unenroll from $courseName",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}
