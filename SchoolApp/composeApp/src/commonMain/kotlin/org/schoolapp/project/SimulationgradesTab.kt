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
import java.lang.String.format

@Composable
fun SimulationgradesTab() {
    var gradeInput by remember { mutableStateOf("") }
    var ratioInput by remember { mutableStateOf("") }
    var maxRatioInput by remember { mutableStateOf("") }
    var scoreInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Average") }
    var averageScore by remember { mutableStateOf<Double?>(null) }
    var isDeleteMode by remember { mutableStateOf(false) }

    val gradesList = remember { mutableStateListOf<GradeItem>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Simulation :",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Subject Name Entry Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = gradeInput,
                onValueChange = { gradeInput = it },
                placeholder = { Text("subject name") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = TextFieldDefaults.textFieldColors()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = ratioInput,
                    onValueChange = { ratioInput = it },
                    placeholder = { Text("Ratio") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = scoreInput,
                    onValueChange = { scoreInput = it },
                    placeholder = { Text("Score") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = maxRatioInput,
                    onValueChange = { maxRatioInput = it },
                    placeholder = { Text("/Total") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    errorMessage = validateInputs(gradeInput, ratioInput, scoreInput, maxRatioInput)
                    if (errorMessage.isEmpty()) {
                        gradesList.add(
                            GradeItem(gradeInput, "${ratioInput}%", scoreInput, maxRatioInput)
                        )
                        gradeInput = ""
                        ratioInput = ""
                        maxRatioInput = ""
                        scoreInput = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF6B6B))
            ) {
                Text(text = "Add", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Load and Delete Button Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TabButton(text = "Average", isSelected = selectedTab == "Average") {
                selectedTab = "Average"
                val totalRatio = gradesList.sumOf { it.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0 }
                if (totalRatio == 100.0) {
                    averageScore = calculateAverageScore(gradesList)
                    errorMessage = ""
                } else {
                    errorMessage = "The total ratio should be 100"
                    averageScore = null
                }
            }
            Button(
                onClick = { /* TODO: Implement Load functionality */ },
                colors = ButtonDefaults.buttonColors(Color.DarkGray)
            ) {
                Text(text = "Load", color = Color.White)
            }
            Button(
                onClick = { isDeleteMode = !isDeleteMode },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Delete", color = Color.White)
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
            gradesList.forEachIndexed { index, item ->
                GradeRow(item, isDeleteMode) {
                    gradesList.removeAt(index)
                }
                Divider(color = Color.Gray.copy(alpha = 0.5f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average Score Display
        if (averageScore != null) {
            Text(
                text = "Average: ${format("%.2f", averageScore)}/20",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

fun calculateAverageScore(gradesList: List<GradeItem>): Double {
    var totalScore = 0.0
    var totalRatio = 0.0

    for (item in gradesList) {
        val ratio = item.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0
        val score = item.score.toDoubleOrNull() ?: 0.0
        val maxScore = item.maxScore.toDoubleOrNull() ?: 1.0

        val scoreConverted = (score / maxScore) * 100
        totalScore += scoreConverted * (ratio / 100)
        totalRatio += ratio
    }

    val finalScore = if (totalRatio > 0) (totalScore / totalRatio) * 20 else 0.0
    return finalScore
}

fun validateInputs(grade: String, ratio: String, score: String, total: String): String {
    if (grade.isBlank()) return "Subject name should be completed"
    if (ratio.isBlank()) return "Ratio should be completed"
    if (score.isBlank()) return "Score should be completed"
    if (total.isBlank()) return "Total should be completed"

    val ratioValue = ratio.toIntOrNull()
    if (ratioValue == null || ratioValue !in 0..100) return "The ratio should be a number between 0 and 100"

    val scoreValue = score.toIntOrNull()
    if (scoreValue == null) return "Your score should only be a number"

    val totalValue = total.toIntOrNull()
    if (totalValue == null) return "The total should only be a number"

    if (scoreValue > totalValue) return "Your score cannot be bigger than the total"

    return ""
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        )
    ) {
        Text(text = text, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun GradeRow(item: GradeItem, isDeleteMode: Boolean, onDelete: () -> Unit) {
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
        if (isDeleteMode) {
            Button(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }
    }
}

data class GradeItem(
    val name: String,
    val ratio: String,
    val score: String,
    val maxScore: String
)