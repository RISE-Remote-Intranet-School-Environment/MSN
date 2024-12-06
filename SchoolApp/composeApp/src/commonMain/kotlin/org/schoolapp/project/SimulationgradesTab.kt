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
    var scoreInput by remember { mutableStateOf("") }
    var targetGradeInput by remember { mutableStateOf("") }
    var targetRatioInput by remember { mutableStateOf("") }
    var targetMaxRatioInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Average") }
    var averageScore by remember { mutableStateOf<Double?>(null) }
    var isDeleteMode by remember { mutableStateOf(false) }
    var showTargetRows by remember { mutableStateOf(false) }
    var selectedTrigram by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val gradesList = remember { mutableStateListOf<GradeItem>() }
    val exampleGrades = listOf(
        Grade("PHY", "Labo 2 : Optic", 20, 14, 20),
        Grade("CHE", "Report Oxydo-reduction", 35, 8, 10),
        Grade("LAW", "Essay Constitution", 60, 10, 30),
        Grade("THE", "Exercices : Bernouilli", 5, 4, 15)
    )

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
            Box {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray)
                ) {
                    Text(text = "Load", color = Color.White)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    exampleGrades.forEach { grade ->
                        DropdownMenuItem(onClick = {
                            selectedTrigram = grade.subject
                            expanded = false
                            loadGrade(gradesList, grade)
                        }) {
                            Text(text = grade.subject)
                        }
                    }
                }
            }
            Button(
                onClick = { isDeleteMode = !isDeleteMode },
                colors = ButtonDefaults.buttonColors(Color.Red)
            ) {
                Text(text = "Delete", color = Color.White)
            }
            TabButton(text = "Target", isSelected = selectedTab == "Target") {
                selectedTab = "Target"
                showTargetRows = !showTargetRows
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Rows
        if (showTargetRows) {
            // Subject Name Entry Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = targetGradeInput,
                    onValueChange = { targetGradeInput = it },
                    placeholder = { Text("subject name") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ratio and Total Entry Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = targetRatioInput,
                    onValueChange = { targetRatioInput = it },
                    placeholder = { Text("Ratio") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = targetMaxRatioInput,
                    onValueChange = { targetMaxRatioInput = it },
                    placeholder = { Text("/Total") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = TextFieldDefaults.textFieldColors()
                )
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        errorMessage = validateInputs(targetGradeInput, targetRatioInput, "0", targetMaxRatioInput)
                        if (errorMessage.isEmpty()) {
                            val totalRatio = gradesList.sumOf { it.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0 } + targetRatioInput.toDouble()
                            if (totalRatio == 100.0) {
                                val requiredScore = calculateRequiredScore(gradesList, targetRatioInput.toDouble(), targetMaxRatioInput.toDouble())
                                gradesList.add(
                                    GradeItem(targetGradeInput, "${targetRatioInput}%", requiredScore, targetMaxRatioInput)
                                )
                                targetGradeInput = ""
                                targetRatioInput = ""
                                targetMaxRatioInput = ""
                            } else {
                                errorMessage = "The total ratio should be 100"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF6B6B))
                ) {
                    Text(text = "Add", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

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
                text = "Average: ${String.format("%.2f", averageScore)}/20",
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

fun loadGrade(gradesList: MutableList<GradeItem>, grade: Grade) {
    gradesList.add(
        GradeItem(grade.assessment, "${grade.ratio}%", grade.score.toString(), grade.total.toString())
    )
}

fun validateInputs(grade: String, ratio: String, score: String, total: String): String {
    if (grade.isBlank()) return "Subject name should be completed"
    if (ratio.isBlank()) return "Ratio should be completed"
    if (score.isBlank()) return "Score should be completed"
    if (total.isBlank()) return "Total should be completed"

    val ratioValue = ratio.toIntOrNull()
    if (ratioValue == null || ratioValue !in 0..100) return "The ratio should be a number between 0 and 100"

    val scoreValue = score.toDoubleOrNull()
    if (scoreValue == null || scoreValue < 0) return "The score should be a number between 0 and the total"

    val totalValue = total.toDoubleOrNull()
    if (totalValue == null) return "The total should only be a number"
    if (scoreValue > totalValue) return "The score should be between 0 and the total"

    return ""
}

fun calculateRequiredScore(gradesList: List<GradeItem>, newRatio: Double, newMaxScore: Double): String {
    // Step 1: Normalize existing scores to a total of 100 and multiply by their ratios
    val totalWeightedScore = gradesList.sumOf { item ->
        val score = item.score.toDoubleOrNull() ?: 0.0
        val maxScore = item.maxScore.toDoubleOrNull() ?: 1.0
        val ratio = item.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0
        val normalizedScore = (score / maxScore) * 100
        normalizedScore * (ratio / 100)
    }

    // Step 2: Calculate the total score needed to achieve an average of 10/20
    val requiredTotalScore = 50.0

    // Step 3: Calculate the score needed for the new course
    val remainingScore = requiredTotalScore - totalWeightedScore
    val requiredScoreOn100 = remainingScore / (newRatio / 100)
    val requiredScore = requiredScoreOn100 * (newMaxScore / 100)

    // Format the result to 2 decimal places
    return String.format("%.2f", requiredScore)
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

fun calculateAverageScore(gradesList: List<GradeItem>): Double {
    var totalScore = 0.0
    var totalRatio = 0.0

    for (item in gradesList) {
        val ratio = item.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0
        val score = item.score.toDoubleOrNull() ?: 0.0
        val maxScore = item.maxScore.toDoubleOrNull() ?: 1.0

        val scoreConverted = (score / maxScore) * 20
        totalScore += scoreConverted * (ratio / 100)
        totalRatio += ratio
    }

    return if (totalRatio > 0) totalScore / (totalRatio / 100) else 0.0
}

data class GradeItem(
    val name: String,
    val ratio: String,
    val score: String,
    val maxScore: String
)

data class Grade(
    val subject: String,
    val assessment: String,
    val ratio: Int,
    val score: Int,
    val total: Int
)