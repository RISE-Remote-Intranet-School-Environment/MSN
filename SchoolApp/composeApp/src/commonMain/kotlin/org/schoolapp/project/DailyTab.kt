package org.schoolapp.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

@Composable
fun DailyTab() {
    var newTask by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf("Complete homework", "Prepare for presentation") }

    var showD_EventDialog by remember { mutableStateOf(false) } // Gérer l'affichage de la pop-up
    var showD_TodoListDialog by remember { mutableStateOf(false) } // Pop-up pour les tâches
    var currentMonth by remember { mutableStateOf(YearMonth.now()) } // Mois courant
    val currentDay = LocalDate.now().dayOfMonth // Jour actuel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5F9EA0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Calendrier avec les boutons <, > et +
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)  // Augmenter la taille du calendrier ici
                .background(Color(0xFF2F4F4F), RoundedCornerShape(12.dp))
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Month", tint = Color.White)
                    }
                    Text(
                        text = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Month", tint = Color.White)
                    }
                }

                // Affichage de la grille du calendrier
                D_CalendarGrid(currentMonth = currentMonth, currentDay = currentDay)
            }

            // Bouton + pour ajouter un événement
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = { showD_EventDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Align the button to the top-right
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton Courses
        Button(
            onClick = { /* Action pour les cours */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFD3D3D3))
        ) {
            Text(text = "Courses")
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Liste des tâches transformée en bouton
        Button(
            onClick = { showD_TodoListDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFD3D3D3))
        ) {
            Text(text = "To Do List")
        }

        // Pop-up pour afficher les tâches et ajouter une nouvelle tâche
        if (showD_TodoListDialog) {
            D_TodoListDialog(tasks = tasks, onDismiss = { showD_TodoListDialog = false })
        }
    }

    // Pop-up pour ajouter un événement personnel
    if (showD_EventDialog) {
        D_EventDialog(onDismiss = { showD_EventDialog = false })
    }
}

@Composable
fun D_CalendarGrid(currentMonth: YearMonth, currentDay: Int) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.lengthOfMonth()

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column {
        // Affichage des jours de la semaine
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            daysOfWeek.forEach {
                Text(
                    text = it,
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        var day = 1 - firstDayOfMonth.dayOfWeek.value
        for (week in 1..6) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (i in 1..7) {
                    val dayText = if (day > 0 && day <= lastDayOfMonth) day.toString() else ""
                    val isToday = day == currentDay
                    val textColor = if (isToday) Color.White else Color.Gray
                    val backgroundColor = if (isToday) Color.Red else Color.Transparent
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(30.dp)
                            .background(backgroundColor, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayText,
                            color = textColor,
                            fontSize = 14.sp
                        )
                    }
                    day++
                }
            }
        }
    }
}

@Composable
fun D_EventDialog(onDismiss: () -> Unit) {
    var eventTitle by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Event") },
        text = {
            Column {
                TextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Event Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Event Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Ajouter l'événement
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun D_TodoListDialog(tasks: MutableList<String>, onDismiss: () -> Unit) {
    var newTask by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("To Do List") },
        text = {
            Column {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tasks) { task ->
                        D_TodoItem(task = task)
                    }
                }

                // Champ pour ajouter une nouvelle tâche
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    TextField(
                        value = newTask,
                        onValueChange = { newTask = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        placeholder = { Text("New task") },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(
                        onClick = {
                            if (newTask.isNotBlank()) {
                                tasks.add(newTask)
                                newTask = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Add")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun D_TodoItem(task: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0C4DE), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Checkbox(checked = false, onCheckedChange = null)
        Text(
            text = task,
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
