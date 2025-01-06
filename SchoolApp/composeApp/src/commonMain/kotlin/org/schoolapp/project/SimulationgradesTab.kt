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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Delete

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
// Fonction de validation des entrées
fun validateInputs(
    gradeInput: String,
    ratioInput: String,
    scoreInput: String,
    maxRatioInput: String
): String {
    return when {
        gradeInput.isEmpty() -> "Subject name is required."
        ratioInput.isEmpty() -> "Ratio is required."
        scoreInput.isEmpty() -> "Score is required."
        maxRatioInput.isEmpty() -> "Max ratio is required."
        ratioInput.toDoubleOrNull() == null -> "Ratio must be a number."
        scoreInput.toDoubleOrNull() == null -> "Score must be a number."
        maxRatioInput.toDoubleOrNull() == null -> "Max ratio must be a number."
        else -> ""
    }
}

// Calcul de la moyenne pondérée
fun calculateAverageScore(gradesList: List<GradeItem>): Double {
    val totalScore = gradesList.sumOf { it.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0 * (it.score.toDoubleOrNull() ?: 0.0) }
    val totalRatio = gradesList.sumOf { it.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0 }
    return if (totalRatio > 0) totalScore / totalRatio * 20 else 0.0
}

// Calcul du score nécessaire pour atteindre un objectif
fun calculateRequiredScore(
    gradesList: List<GradeItem>,
    targetRatio: Double,
    targetMaxRatio: Double
): String {
    val totalRatio = gradesList.sumOf { it.ratio.removeSuffix("%").toDoubleOrNull() ?: 0.0 }
    val remainingRatio = 100.0 - totalRatio
    return if (remainingRatio > 0) {
        val requiredScore = (targetMaxRatio * remainingRatio) / targetRatio
        String.format("%.2f", requiredScore)
    } else {
        "N/A"
    }
}

// Fonction pour générer un bouton de tabulation
@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .background(if (isSelected) Color.Gray else Color.LightGray, shape = RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
    }
}

// Fonction pour afficher une ligne de la liste de grades
@Composable
fun GradeRow(item: GradeItem, isDeleteMode: Boolean, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.ratio,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.score,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.maxScore,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (isDeleteMode) {
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

