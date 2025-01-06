package org.schoolapp.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReportTab() {
    val reportItems = getGroupedGrades() // Fetch the data using the function from data

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Report Card:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Report Card Table
        Card(
            modifier = Modifier
                .fillMaxWidth(1f) // Adjust the width fraction here
                .padding(horizontal = 16.dp, vertical = 8.dp),
            backgroundColor = Color(0xFFB0C4DE),
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                reportItems.forEach { item ->
                    ReportCardRow(item)
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average Grade
        val overallAverage = reportItems.map { it.grade.toDouble() }.average()
        Text(
            text = "Average: ${overallAverage.toInt()}/20",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun ReportCardRow(item: ReportItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Subject Trigram and Name
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = item.trigram,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.subject,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ECTS Points, Grade, and Total Score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${item.ects} ECTS",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Row {
                Text(
                    text = item.grade.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.grade >= item.total / 2) Color(0xFF228B22) else Color.Red
                )
                Text(
                    text = "/ ${item.total}",
                    fontSize = 16.sp
                )
            }
        }
    }
}
