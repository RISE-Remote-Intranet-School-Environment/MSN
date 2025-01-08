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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material.Card
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.saveable.*
import androidx.compose.runtime.saveable.rememberSaveable
import android.content.Context
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.Delete

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable



@Composable
fun MonthlyTab() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var currentDay by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var events by remember { mutableStateOf(generateEvents()) }

    val tasks = remember { mutableStateListOf(*generateToDoItems().toTypedArray()) }
    var showEventDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showToDoListDialog by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }
    var showEventListDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
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

                CalendarGrid(
                    currentMonth = currentMonth,
                    currentDay = currentDay,
                    events = events,
                    onDayClick = { selectedDate = it; showEventListDialog = true }
                )
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
            if (showToDoListDialog) {
                val context = LocalContext.current  // Récupérer le contexte actuel
                ToDoListDialog(tasks = tasks, onDismiss = { showToDoListDialog = false }, context = context)
            }

            // Display event list dialog when a day is clicked
            if (showEventListDialog) {
                EventListDialog(
                    selectedDate = selectedDate,
                    events = events,
                    onDismiss = { showEventListDialog = false },
                    onAddEventClick = {
                        showEventDialog = true
                        showEventListDialog = false
                    }
                )
            }

            if (showEventDialog) {
                EventDialog(
                    selectedDate = selectedDate,
                    onDismiss = { showEventDialog = false },
                    onEventAdded = { event ->
                        events = events.toMutableMap().apply {
                            put(
                                selectedDate,
                                (events[selectedDate]?.toMutableList() ?: mutableListOf()).apply {
                                    add(event)
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    currentDay: Int,
//    events: MutableMap<LocalDate, List<Event>>,
    events: MutableMap<LocalDate, MutableList<Event>>,
    onDayClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.lengthOfMonth()

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    // Calcul de la hauteur disponible pour la grille des jours
    val availableHeight = 400.dp // Hauteur totale de 500.dp pour la grille
    val headerHeight = 48.dp // Espace réservé pour l'en-tête avec les jours de la semaine (Mon, Tue, etc.)
    val gridHeight = availableHeight - headerHeight // Hauteur restante pour les jours

    val totalDays = lastDayOfMonth
    val rows = (totalDays + firstDayOfMonth.dayOfWeek.value - 1) / 7 + 1
    // Calcul de la taille de chaque cellule de jour en fonction de la hauteur disponible
    val dayCellHeight = gridHeight / rows

    Column {
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
                    val date = if (day > 0 && day <= totalDays) firstDayOfMonth.plusDays((day - 1).toLong()) else null

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(dayCellHeight)
                            .weight(1f)
                            .background(backgroundColor, RoundedCornerShape(15.dp))
                            .clickable { date?.let { onDayClick(it) } },
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
fun EventListDialog(
    selectedDate: LocalDate,
    events: MutableMap<LocalDate, MutableList<Event>>,
    onDismiss: () -> Unit,
    onAddEventClick: () -> Unit
) {
    val eventsForDay = events[selectedDate]?.filter { it.isVisible } ?: mutableListOf()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Events for ${selectedDate.toString()}") },
        text = {
            Column {
                LazyColumn {
                    items(eventsForDay) { event ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
                            elevation = 4.dp
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = " ${event.title}", style = MaterialTheme.typography.h6)
                                Text(text = " ${event.description}", style = MaterialTheme.typography.body2)
                                Text(text = " Start: ${event.startTime}")
                                Text(text = " End: ${event.endTime}")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = {
                                            event.isVisible = false  // Set event visibility to false
                                            if (eventsForDay.isEmpty()) {
                                                events.remove(selectedDate) // Optionally remove the date if no events are left
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Event",
                                            tint = Color.Red,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onAddEventClick) {
                Text("Add Event")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}


@Composable
fun EventDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var eventTitle by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventStartTime by remember { mutableStateOf("") }
    var eventEndTime by remember { mutableStateOf("") }
    var expandedStartTime by remember { mutableStateOf(false) }
    var expandedEndTime by remember { mutableStateOf(false) }

    // Liste des heures disponibles (9 à 18 heures)
    val availableHours = (0..24).map { "${it}h00" }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Event for ${selectedDate.toString()} \n ",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F4F4F),
                fontSize = 18.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Titre de l'événement
                TextField(
                    value = eventTitle,
                    onValueChange = { eventTitle = it },
                    label = { Text("Event Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFE8F5E9))
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Description de l'événement
                TextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Event Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFE8F5E9))
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Heure de début
                Text("Event Start Time", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedStartTime = !expandedStartTime }
                        .padding(8.dp)
                        .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = eventStartTime.ifEmpty { "Select Start Time" },
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expandedStartTime,
                    onDismissRequest = { expandedStartTime = false }
                ) {
                    availableHours.forEach { hour ->
                        DropdownMenuItem(onClick = {
                            eventStartTime = hour
                            expandedStartTime = false
                        }) {
                            Text(text = hour)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Heure de fin
                Text("Event End Time", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedEndTime = !expandedEndTime }
                        .padding(8.dp)
                        .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = eventEndTime.ifEmpty { "Select End Time" },
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black
                    )
                }

                DropdownMenu(
                    expanded = expandedEndTime,
                    onDismissRequest = { expandedEndTime = false }
                ) {
                    availableHours.forEach { hour ->
                        DropdownMenuItem(onClick = {
                            eventEndTime = hour
                            expandedEndTime = false
                        }) {
                            Text(text = hour)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newEvent = Event(eventTitle, eventDescription, eventStartTime, eventEndTime, color = Color.Gray)
                    onEventAdded(newEvent)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00796B))
            ) {
                Text("Add",color = Color(0xFFFFFFFF),
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF00796B))
            }
        }
    )
}

@Composable
fun ToDoListDialog(
    tasks: MutableList<ToDoItem>,  // Change le type en MutableList<ToDoItem>
    onDismiss: () -> Unit,
    context: Context
) {
    var newTask by remember { mutableStateOf("") }
    val taskStates = rememberSaveable { loadTaskStates(context, tasks.size) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("To Do List\n") },
        text = {
            Column {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(tasks) { index, task ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(8.dp) // Ajuster le padding
                        ) {
                            ToDoItem(
                                task = task.title,  // Utiliser `task.title` pour afficher le titre de la tâche
                                isChecked = taskStates[index],
                                onCheckedChange = { isChecked ->
                                    taskStates[index] = isChecked
                                    saveTaskStates(context, taskStates)
                                }
                            )
                            Spacer(modifier = Modifier.weight(1f)) // Ajouter un espace pour forcer l'icône à se placer à droite
                            IconButton(
                                onClick = {
                                    tasks.removeAt(index) // Supprime la tâche
                                    taskStates.removeAt(index) // Supprime l'état associé
                                    saveTaskStates(context, taskStates)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Task",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp) // Taille plus petite
                                )
                            }
                        }

                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    TextField(
                        value = newTask,
                        onValueChange = { newTask = it },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        placeholder = { Text("New task") },
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (newTask.isNotBlank()) {
                                tasks.add(ToDoItem(newTask, false)) // Ajouter un ToDoItem
                                taskStates.add(false)
                                newTask = ""
                                saveTaskStates(context, taskStates)
                            }
                        }
                    ) {
                        Text("Add")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}


// Fonction pour sauvegarder l'état des tâches dans SharedPreferences
fun saveTaskStates(context: Context, taskStates: List<Boolean>) {
    val sharedPreferences = context.getSharedPreferences("todo_preferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    // Sauvegarde sous forme de chaîne, avec chaque valeur séparée par une virgule
    editor.putString("task_states", taskStates.joinToString(","))
    editor.apply()
}

// Fonction pour charger l'état des tâches depuis SharedPreferences
fun loadTaskStates(context: Context, taskCount: Int): MutableList<Boolean> {
    val sharedPreferences = context.getSharedPreferences("todo_preferences", Context.MODE_PRIVATE)
    val savedState = sharedPreferences.getString("task_states", "")
    val states = savedState?.split(",")?.map { it.toBoolean() }?.toMutableList() ?: mutableListOf()
    // Remplir la liste avec des valeurs par défaut si l'état est incomplet
    while (states.size < taskCount) {
        states.add(false)
    }
    return states
}

@Composable
fun ToDoItem(task: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB0C4DE), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) }
        )
        Text(
            text = task,
            color = if (isChecked) Color.Gray else Color.Black,  // Changer la couleur si cochée
            modifier = Modifier
                .padding(start = 8.dp)
                .graphicsLayer(alpha = if (isChecked) 0.5f else 1f)  // Utiliser graphicsLayer pour l'opacité
        )
    }
}




