package org.schoolapp.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CourseDetailsPage(courseName: String, onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Details", "Files")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Course: $courseName") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to My Courses")
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        content = {
            when (selectedTab) {
                0 -> CourseDetailsTab(courseName) // Onglet Détails
                1 -> CourseFilesTab(courseName)   // Onglet Fichiers
            }
        }
    )
}

@Composable
fun CourseDetailsTab(courseName: String) {
    val details = courseDetails[courseName] ?: "Details for this course are not available."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Course Details for $courseName",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Overview:",
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = details,
            style = MaterialTheme.typography.body1,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "What You Will Learn:",
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "1. Key topics in ${courseName.lowercase()}.\n2. Hands-on projects.\n3. Critical thinking and analysis skills.",
            style = MaterialTheme.typography.body1,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun CourseFilesTab(courseName: String) {
    val files = courseFiles[courseName] ?: listOf("No files available.")
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(files) { file ->
            Text(file, modifier = Modifier.padding(8.dp))
        }
    }
}
