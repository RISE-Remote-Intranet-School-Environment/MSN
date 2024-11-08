package org.schoolapp.project
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Grade(
    val subject: String,
    val assessment: String,
    val score: Int,
    val total: Int
)

@Composable
fun RecentgradesTab() {
    val grades = listOf(
        Grade("PHY", "Labo 2", 14, 20),
        Grade("CHE", "Report", 8, 10),
        Grade("LAW", "Essay", 10, 30),
        Grade("THE", "Exercices", 4, 15)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF87CEEB)) // Light blue background
    ) {
        // Top Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Validated ECTS
        Text(
            "Validated : 30 ECTS",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Grades Title and Filter Button
        Text(
            "Recent Grades:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* Filter action */ },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2F4F4F)),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Filter", color = Color.White)
        }

        // Grades List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(grades) { grade ->
                GradeCard(grade)
            }
        }
    }
}

@Composable
fun GradeCard(grade: Grade) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        backgroundColor = Color(0xFFADD8E6) // Light blue card background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Subject and Assessment
            Text(
                "${grade.subject} : ${grade.assessment}",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Score and Total
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = grade.score.toString(),
                    color = if (grade.score >= grade.total / 2) Color(0xFF228B22) else Color.Red, // Green for passing, red for failing
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = " / ${grade.total}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
        }
    }
}
