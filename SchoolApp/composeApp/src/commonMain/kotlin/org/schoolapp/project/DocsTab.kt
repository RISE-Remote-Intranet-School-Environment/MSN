import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DocsTab() {
    val docTypes = listOf("Summaries", "Solutions", "Tests & Exams", "Videos")
    var selectedDocType by remember { mutableStateOf(docTypes.first()) }
    var selectedGrade by remember { mutableStateOf("All Grades") }
    var selectedYear by remember { mutableStateOf("All Years") }
    var selectedLesson by remember { mutableStateOf("All Lessons") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Document Type Selector
        Text("Document Types", style = MaterialTheme.typography.h6)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(docTypes) { docType ->
                ListItem(
                    text = { Text(docType) },
                    modifier = Modifier.clickable { selectedDocType = docType }
                )
                Divider()
            }
        }

        // Filters
        Spacer(modifier = Modifier.height(16.dp))
        Text("Filters", style = MaterialTheme.typography.h6)

        // Filter by Grade
        DropdownFilter(
            label = "Grade",
            options = listOf("All Grades", "Grade 1", "Grade 2", "Grade 3"),
            selectedOption = selectedGrade,
            onOptionSelected = { selectedGrade = it }
        )

        // Filter by Year
        DropdownFilter(
            label = "Year",
            options = listOf("All Years", "2020", "2021", "2022", "2023", "2024"),
            selectedOption = selectedYear,
            onOptionSelected = { selectedYear = it }
        )

        // Filter by Lesson
        DropdownFilter(
            label = "Lesson",
            options = listOf("All Lessons", "Math", "Physics", "Chemistry", "Programming"),
            selectedOption = selectedLesson,
            onOptionSelected = { selectedLesson = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtered Document List
        Text("Filtered Documents for $selectedDocType", style = MaterialTheme.typography.h6)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(getFilteredDocuments(selectedDocType, selectedGrade, selectedYear, selectedLesson)) { document ->
                Text(text = document, style = MaterialTheme.typography.body1)
                Divider()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { /* Action to share a document */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Share a doc")
        }
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
    Column {
        Text(label, style = MaterialTheme.typography.subtitle1)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedOption)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }) {
                        Text(option)
                    }
                }
            }
        }
    }
}

// Mock function to simulate filtered documents
fun getFilteredDocuments(docType: String, grade: String, year: String, lesson: String): List<String> {
    // Replace with actual filtering logic
    return listOf(
        "$docType Document 1 for $lesson - $grade - $year",
        "$docType Document 2 for $lesson - $grade - $year",
        "$docType Document 3 for $lesson - $grade - $year"
    )
}
