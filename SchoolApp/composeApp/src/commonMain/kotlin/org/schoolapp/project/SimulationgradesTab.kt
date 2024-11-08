package org.schoolapp.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SimulationgradesTab() {
    var gradeInput by remember { mutableStateOf("") }
    var ratioInput by remember { mutableStateOf("") }
    var maxRatioInput by remember { mutableStateOf("") }
    var average by remember { mutableStateOf("13.6") }
    var selectedTab by remember { mutableStateOf("Average") }

    val gradesList = remember {
        mutableStateListOf(
            GradeItem("Labo 1", "40%", "24", "30"),
            GradeItem("Labo 2", "40%", "18", "30"),
            GradeItem("Exercice 1", "10%", "2", "5"),
            GradeItem("Exercice 2", "10%", "4", "5")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5F9EA0)) // Background color similar to the screenshot
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Simulation :",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Load Button
        Button(
            onClick = { /* TODO: Implement Load functionality */ },
            modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(Color.DarkGray)
        ) {
            Text(text = "Load", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = gradeInput,
                onValueChange = { gradeInput = it },
                placeholder = { Text("Insert a new grade ...") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(

                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = ratioInput,
                onValueChange = { ratioInput = it },
                placeholder = { Text("Ratio") },
                singleLine = true,
                modifier = Modifier.width(50.dp),
                colors = TextFieldDefaults.textFieldColors(

                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = maxRatioInput,
                onValueChange = { maxRatioInput = it },
                placeholder = { Text("/..") },
                singleLine = true,
                modifier = Modifier.width(50.dp),
                colors = TextFieldDefaults.textFieldColors(

                )
            )
            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    // TODO: Add logic to add the new grade to the list
                    if (gradeInput.isNotBlank() && ratioInput.isNotBlank() && maxRatioInput.isNotBlank()) {
                        gradesList.add(
                            GradeItem(gradeInput, "${ratioInput}%", "0", maxRatioInput)
                        )
                        gradeInput = ""
                        ratioInput = ""
                        maxRatioInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF6B6B))
            ) {
                Text(text = "Add", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs for Average and Target
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabButton(text = "Average", isSelected = selectedTab == "Average") {
                selectedTab = "Average"
            }
            TabButton(text = "Target", isSelected = selectedTab == "Target") {
                selectedTab = "Target"
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grades Table
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB0C4DE), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            gradesList.forEach { item ->
                GradeRow(item)
                Divider(color = Color.Gray.copy(alpha = 0.5f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average Display
        Text(
            text = "Average : $average/20",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(

            contentColor = Color.White
        ),

        ) {
        Text(text = text, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun GradeRow(item: GradeItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = item.ratio, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = item.score, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = item.maxScore, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
}

data class GradeItem(
    val name: String,
    val ratio: String,
    val score: String,
    val maxScore: String
)
