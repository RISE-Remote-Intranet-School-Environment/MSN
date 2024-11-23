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

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.School



@Composable
fun MonthlyTab() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var currentDay by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var newTask by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf("Complete homework", "Prepare for presentation") }

    var showEventDialog by remember { mutableStateOf(false) }
    var showToDoListDialog by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }


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
                .height(550.dp)  // Augmenter la taille du calendrier ici
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
                CalendarGrid(currentMonth = currentMonth, currentDay = currentDay)
            }
            // Bouton flottant avec effet pour afficher les options
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    AnimatedVisibility(
                        visible = showOptions,
                        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { it / 2 }),
                        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { it / 2 })
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            // Bouton To Do List
                            FloatingActionButton(
                                onClick = { showToDoListDialog = true },
                                backgroundColor = Color(0xFFB0C4DE),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(Icons.Filled.List, contentDescription = "To Do List", tint = Color.Black)
                            }

                            // Bouton Cours
                            FloatingActionButton(
                                onClick = { /* Action pour les cours */ },
                                backgroundColor = Color(0xFFB0C4DE),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(Icons.Filled.School, contentDescription = "Courses", tint = Color.Black)
                            }

                            // Nouveau bouton pour Ajouter un événement
                            FloatingActionButton(
                                onClick = { showEventDialog = true },
                                backgroundColor = Color(0xFFB0C4DE),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add Event", tint = Color.Black)
                            }
                        }
                    }

                    // Bouton flottant principal qui déclenche l'animation
                    FloatingActionButton(
                        onClick = { showOptions = !showOptions },
                        backgroundColor = Color(0xFF5F9EA0)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Options", tint = Color.White)
                    }
                }
            }

            // Dialogues pour To Do List et ajout d'événements
            if (showEventDialog) {
                EventDialog(onDismiss = { showEventDialog = false })
            }
            if (showToDoListDialog) {
                ToDoListDialog(tasks = tasks, onDismiss = { showToDoListDialog = false })
            }

        }

    }
}

@Composable
fun CalendarGrid(currentMonth: YearMonth, currentDay: Int) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.lengthOfMonth()

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Calcul de la hauteur disponible pour la grille des jours
    val availableHeight = 400.dp // Hauteur totale de 500.dp pour la grille
    val headerHeight = 48.dp // Espace réservé pour l'en-tête avec les jours de la semaine (Mon, Tue, etc.)
    val gridHeight = availableHeight - headerHeight // Hauteur restante pour les jours

    // Nombre total de jours dans le mois
    val totalDays = lastDayOfMonth

    // Calcul du nombre de lignes nécessaires
    val rows = (totalDays + firstDayOfMonth.dayOfWeek.value - 1) / 7 + 1

    // Calcul de la taille de chaque cellule de jour en fonction de la hauteur disponible
    val dayCellHeight = gridHeight / rows

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
        for (week in 1..rows) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (i in 1..7) {
                    val dayText = if (day > 0 && day <= totalDays) day.toString() else ""
                    val isToday = day == currentDay
                    val textColor = if (isToday) Color.White else Color.Gray
                    val backgroundColor = if (isToday) Color.Red else Color.Transparent
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(dayCellHeight) // Utilisation de la hauteur calculée pour chaque cellule
                            .weight(1f) // Utilisation de `weight` pour une taille proportionnelle
                            .background(backgroundColor, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayText,
                            color = textColor,
                            fontSize = 14.sp // Tu peux ajuster la taille de la police ici si nécessaire
                        )
                    }
                    day++
                }
            }
        }
    }
}


@Composable
fun EventDialog(onDismiss: () -> Unit) {
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
fun ToDoListDialog(tasks: MutableList<String>, onDismiss: () -> Unit) {
    var newTask by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("To Do List") },
        text = {
            Column {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tasks) { task ->
                        ToDoItem(task = task)
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
fun ToDoItem(task: String) {
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
