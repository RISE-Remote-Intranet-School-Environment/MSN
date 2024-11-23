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
fun WeeklyTab() {
    var newTask by remember { mutableStateOf("") }
    val tasks = remember { mutableStateListOf("Complete homework", "Prepare for presentation") }

    var showW_EventDialog by remember { mutableStateOf(false) }
    var showW_TodoListDialog by remember { mutableStateOf(false) }
    var startOfWeek by remember { mutableStateOf(LocalDate.now().with(java.time.DayOfWeek.MONDAY)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5F9EA0))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Affichage des boutons de navigation par semaine
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)  // Taille fixe pour le calendrier avec scroll
                .background(Color(0xFF2F4F4F), RoundedCornerShape(12.dp))
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { startOfWeek = startOfWeek.minusWeeks(1) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Week", tint = Color.White)
                    }
                    Text(
                        text = "Semaine du ${startOfWeek.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = { startOfWeek = startOfWeek.plusWeeks(1) }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Week", tint = Color.White)
                    }
                }

                // Grille hebdomadaire avec les heures défilable verticalement
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)  // Hauteur fixe
                        .verticalScroll(rememberScrollState())  // Scroll vertical
                ) {
                    W_WeeklyGrid(startOfWeek)
                }
            }

            // Bouton + pour ajouter un événement
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = { showW_EventDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event", tint = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton To Do List
        Button(
            onClick = { showW_TodoListDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFD3D3D3))
        ) {
            Text(text = "To Do List")
        }

        if (showW_TodoListDialog) {
            W_TodoListDialog(tasks = tasks, onDismiss = { showW_TodoListDialog = false })
        }
    }

    if (showW_EventDialog) {
        W_EventDialog(onDismiss = { showW_EventDialog = false })
    }
}

@Composable
fun W_WeeklyGrid(startOfWeek: LocalDate) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val hours = (8..24).toList()

    Row(Modifier.fillMaxSize()) {
        // Colonne des heures
        Column(
            modifier = Modifier
                .width(50.dp)
                .padding(top = 20.dp)
        ) {
            hours.forEach { hour ->
                Text(
                    text = "${hour.toString().padStart(2, '0')}:00",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp) // Ajustement du padding vertical pour une grille plus fine
                )
            }
        }

        // Colonnes des jours de la semaine sans décalage
        daysOfWeek.forEachIndexed { index, day ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp) // Réduction de padding pour affiner la grille
            ) {
                val currentDate = startOfWeek.plusDays(index.toLong())
                Text(
                    text = "$day ${currentDate.dayOfMonth}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(4.dp)
                )

                // Grille horaire pour chaque jour
                hours.forEach {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp) // Taille réduite de la cellule
                            .background(Color(0xFF8A8A8A), RoundedCornerShape(2.dp)) // Coins légèrement arrondis pour un style plus léger
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "", color = Color.White)
                    }
                }
            }
        }
    }
}




@Composable
fun W_EventDialog(onDismiss: () -> Unit) {
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
fun W_TodoListDialog(tasks: MutableList<String>, onDismiss: () -> Unit) {
    var newTask by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("To Do List") },
        text = {
            Column {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tasks) { task ->
                        W_TodoItem(task = task)
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
fun W_TodoItem(task: String) {
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
