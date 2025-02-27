package org.schoolapp.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun DocsTab() {
    var selectedDocumentType by remember { mutableStateOf("No Type") }
    var selectedGrade by remember { mutableStateOf("No Grades") }
    var selectedYear by remember { mutableStateOf("No Years") }
    var selectedLesson by remember { mutableStateOf("No Lessons") }
    var showShareDialog by remember { mutableStateOf(false) }

    val summaries = remember { mutableStateListOf<String>() }
    val solutions = remember { mutableStateListOf<String>() }
    val exams = remember { mutableStateListOf<String>() }

    val currentDocuments = when (selectedDocumentType) {
        "Summaries" -> summaries
        "Solutions" -> solutions
        "Tests & Exams" -> exams
        else -> emptyList()
    }

    val filteredDocuments = currentDocuments.filter { document ->
        (selectedGrade != "No Grades" && document.contains(selectedGrade)) &&
                (selectedYear != "No Years" && document.contains(selectedYear)) &&
                (selectedLesson != "No Lessons" && document.contains(selectedLesson))
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showShareDialog = true },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Share a doc", tint = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Carte des filtres
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Filters", style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))

                    // Filtre par Type de document
                    DropdownFilter(
                        label = "Document Type",
                        options = listOf("No Type", "Summaries", "Solutions", "Tests & Exams"),
                        selectedOption = selectedDocumentType,
                        onOptionSelected = { selectedDocumentType = it }
                    )

                    // Filtre par Grade
                    DropdownFilter(
                        label = "Grade",
                        options = listOf("No Grades", "Grade 1", "Grade 2", "Grade 3"),
                        selectedOption = selectedGrade,
                        onOptionSelected = { selectedGrade = it }
                    )

                    // Filtre par Year
                    DropdownFilter(
                        label = "Year",
                        options = listOf("No Years", "2024", "2023"),
                        selectedOption = selectedYear,
                        onOptionSelected = { selectedYear = it }
                    )

                    // Filtre par Lesson
                    DropdownFilter(
                        label = "Lesson",
                        options = listOf("No Lessons", "Math", "Python", "English"),
                        selectedOption = selectedLesson,
                        onOptionSelected = { selectedLesson = it }
                    )
                }
            }

            // Liste des documents filtrés
            if (filteredDocuments.isNotEmpty()) {
                Text(
                    text = "Filtered Documents for $selectedDocumentType",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredDocuments) { document ->
                        DocumentCard(document = document)
                    }
                }
            } else {
                // Message si aucun document n'est filtré
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No documents to display. Please apply filters.")
                }
            }
        }
    }

    // Dialog pour partager un document
    if (showShareDialog) {
        ShareDocumentDialog(
            onDismiss = { showShareDialog = false },
            onDocumentShared = { name, grade, year, lesson, type ->
                val newDocument = "$name - $lesson - $grade - $year"
                when (type) {
                    "Summaries" -> summaries.add(newDocument)
                    "Solutions" -> solutions.add(newDocument)
                    "Tests & Exams" -> exams.add(newDocument)
                }
                showShareDialog = false
            }
        )
    }
}

@Composable
fun DropdownFilter(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedOption, color = MaterialTheme.colors.primary)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onOptionSelected(option)
                    }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentCard(document: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = document,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ShareDocumentDialog(
    onDismiss: () -> Unit,
    onDocumentShared: (String, String, String, String, String) -> Unit
) {
    var documentName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Summaries") }
    var selectedGrade by remember { mutableStateOf("Grade 1") }
    var selectedYear by remember { mutableStateOf("2024") }
    var selectedLesson by remember { mutableStateOf("Math") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share a Document") },
        text = {
            Column {
                OutlinedTextField(
                    value = documentName,
                    onValueChange = { documentName = it },
                    label = { Text("Document Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownFilter(
                    label = "Document Type",
                    options = listOf("Summaries", "Solutions", "Tests & Exams"),
                    selectedOption = selectedType,
                    onOptionSelected = { selectedType = it }
                )
                DropdownFilter(
                    label = "Grade",
                    options = listOf("Grade 1", "Grade 2", "Grade 3"),
                    selectedOption = selectedGrade,
                    onOptionSelected = { selectedGrade = it }
                )
                DropdownFilter(
                    label = "Year",
                    options = listOf("2024", "2023"),
                    selectedOption = selectedYear,
                    onOptionSelected = { selectedYear = it }
                )
                DropdownFilter(
                    label = "Lesson",
                    options = listOf("Math", "Python", "English"),
                    selectedOption = selectedLesson,
                    onOptionSelected = { selectedLesson = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onDocumentShared(documentName, selectedGrade, selectedYear, selectedLesson, selectedType)
            }) {
                Text("Share")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
