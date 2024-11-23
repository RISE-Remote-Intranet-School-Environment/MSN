package org.schoolapp.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data class for Tutor
data class Tutor(val name: String, val rating: Int, val courses: Int)

@Composable
fun PrivateLessonsTab() {
    // Sample data
    val tutors = remember {
        listOf(
            Tutor("Mme Dupont", 4, 3),
            Tutor("M. Martin", 5, 5),
            Tutor("Mme Durand", 4, 4),
            Tutor("M. Lefevre", 3, 2),
            Tutor("Mme Moreau", 5, 6)
        )
    }

    // Main column layout
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Private Lessons",
            style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // LazyColumn to list tutors
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(tutors) { tutor ->
                TutorItem(tutor = tutor)
                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }

        // Input field at the bottom
        TextField(
            value = "",
            onValueChange = { /* Handle input */ },
            placeholder = { Text("Search for tutors...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

@Composable
fun TutorItem(tutor: Tutor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Image
        Image(
            painter = painterResource(id = R.drawable.ok), // Replace with your avatar image resource
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .padding(end = 16.dp)

        )

        // Tutor details: Name, Courses, and Stars
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = tutor.name,
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Courses: ${tutor.courses}",
                style = MaterialTheme.typography.body2.copy(color = Color.Gray)
            )
            RatingBar(rating = tutor.rating)
        }

        // Arrow icon for navigation
        IconButton(
            onClick = { /* Navigate to tutor details */ },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "More details",
                tint = Color.Gray
            )
        }
    }
}



