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
import java.time.format.DateTimeFormatter
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

import java.time.DayOfWeek
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalDensity



@Composable
fun WeeklyTab() {
    var currentWeekStart by remember { mutableStateOf(LocalDate.now().with(DayOfWeek.MONDAY)) }
    var currentDay by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var events by remember { mutableStateOf(mutableMapOf<LocalDate, List<Event>>()) }

    LaunchedEffect(Unit) {
        events = generateSequence(LocalDate.of(2024, 11, 1)) { it.plusDays(1) }
            .takeWhile { it.isBefore(LocalDate.of(2026, 1, 1)) }
            .associate { date ->
                date to listOf(
                    Event(
                        title = "Control Theory",
                        description = "Au 2F10 avec DBR",
                        startTime = "08h00",
                        endTime = "12h00"
                    ),
                    Event(
                        title = "Software Architecture",
                        description = "Au 1E04 avec J3L",
                        startTime = "13h00",
                        endTime = "16h00"
                    ),
                    Event(
                        title = "Network Concepts",
                        description = "Au 2D52 avec DSM",
                        startTime = "17h00",
                        endTime = "19h00"
                    )
                )
            }
            .toMutableMap()
    }

    val tasks = remember { mutableStateListOf("Complete homework", "Prepare for presentation") }
    var showEventDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showToDoListDialog by remember { mutableStateOf(false) }
    var showEventListDialog by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5F9EA0))
            .padding(16.dp)
    ) {
        //Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .background(Color(0xFF2F4F4F), RoundedCornerShape(12.dp))
        )
        {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { currentWeekStart = currentWeekStart.minusWeeks(1) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous Week",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "From ${currentWeekStart.format(DateTimeFormatter.ofPattern("dd/MM"))} to ${
                            currentWeekStart.plusDays(
                                6
                            ).format(DateTimeFormatter.ofPattern("dd/MM"))
                        }",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = { currentWeekStart = currentWeekStart.plusWeeks(1) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next Week",
                            tint = Color.White
                        )
                    }
                }

                //Spacer(modifier = Modifier.height(8.dp))

                // Grille hebdomadaire
                W_CalendarGrid(
                    currentWeekStart = currentWeekStart,
                    currentDay = currentDay,
                    events = events,
                    onDayClick = { selectedDate = it; showEventListDialog = true }
                )
            }

                //Bouton flottant avec effet pour afficher les options
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        AnimatedVisibility(
                            visible = showOptions,
                            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                                initialOffsetY = { it / 2 }),
                            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                                targetOffsetY = { it / 2 })
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                FloatingActionButton(
                                    onClick = { showToDoListDialog = true },
                                    backgroundColor = Color(0xFFB0C4DE),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.List,
                                        contentDescription = "To Do List",
                                        tint = Color.Black
                                    )
                                }

                                FloatingActionButton(
                                    onClick = { /* Action pour les cours */ },
                                    backgroundColor = Color(0xFFB0C4DE),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.School,
                                        contentDescription = "Courses",
                                        tint = Color.Black
                                    )
                                }
                            }
                        }

                        FloatingActionButton(
                            onClick = { showOptions = !showOptions },
                            backgroundColor = Color(0xFF5F9EA0)
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Options",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Dialogues pour To Do List et ajout d'événements
                if (showToDoListDialog) {
                    val context = LocalContext.current
                    ToDoListDialog(
                        tasks = tasks,
                        onDismiss = { showToDoListDialog = false },
                        context = context
                    )
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
                                    (events[selectedDate] ?: emptyList()) + event
                                )
                            }
                        }
                    )
                }
            }
        }
    }


@Composable
fun W_CalendarGrid(
    currentWeekStart: LocalDate,
    currentDay: Int,
    events: MutableMap<LocalDate, List<Event>>,
    onDayClick: (LocalDate) -> Unit
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
    val hours = (0..24).toList() // De 0h à 24h
    val weekDays = (0..4).map { currentWeekStart.plusDays(it.toLong()) }
    val scrollState = rememberScrollState() // État de défilement vertical partagé

    // Calcul de la hauteur de chaque heure (en dp) en fonction du nombre total d'heures et de la hauteur totale
    val hourHeightDp = 1440.dp / 24

    Column(Modifier.fillMaxSize()) {
        // En-têtes des jours (fixes)
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(0.15f)) // Place pour les heures
            weekDays.forEachIndexed { index, date ->
                Column(
                    modifier = Modifier
                        .weight(0.85f / 5f)
                        .padding(4.dp)
                        .background(
                            color = if (date.dayOfMonth == currentDay) Color.Red else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onDayClick(date) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${daysOfWeek[index]}\n${date.dayOfMonth}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Contenu défilable (heures + événements)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Remplit tout l'espace disponible
                .verticalScroll(scrollState)
        ) {
            // Colonne des heures
            Column(
                modifier = Modifier
                    .weight(0.15f)
            ) {
                hours.forEach { hour ->
                    val formattedHour = String.format("%02d", hour)
                    Text(
                        text = "$formattedHour:00",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(hourHeightDp) // La hauteur des heures est maintenant dynamique
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    )
                }
            }

            // Colonnes des événements
            Row(
                modifier = Modifier.weight(0.85f)
            ) {
                weekDays.forEach { date ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(1440.dp) // Hauteur totale pour 24 heures
                    ) {
                        val density = LocalDensity.current // Récupération du contexte de densité
                        val totalHeightPx = maxHeight.value * density.density // Conversion de Dp en pixels

                        // Affichage des événements pour chaque date
                        events[date]?.forEach { event ->
                            // Calcul des offsets en fonction du temps et de la hauteur totale
                            val startOffsetPx = timeToOffset(event.startTime, totalHeightPx) + 30f
                            val endOffsetPx = timeToOffset(event.endTime, totalHeightPx) + 50f
                            val eventHeightPx = endOffsetPx - startOffsetPx

                            // Conversion des pixels en Dp
                            val startOffset = startOffsetPx / density.density // Conversion de pixels en Dp
                            val eventHeight = eventHeightPx / density.density // Conversion de pixels en Dp

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = startOffset.dp) // Placement de l'événement
                                    .height(eventHeight.dp) // Hauteur de l'événement
                                    .background(
                                        color = Color(0xFF87CEFA),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = event.title,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = event.description,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "${event.startTime} - ${event.endTime}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun timeToOffset(time: String, totalHeightPx: Float): Float {
    // Suppression du caractère 'h' et séparation de l'heure et des minutes
    val (hour, minute) = time.split("h").map { it.toInt() }

    val totalMinutesInDay = 24 * 60
    val eventStartTimeInMinutes = hour * 60 + minute

    // Calcul du décalage proportionnel en fonction du totalHeightPx (en pixels)
    return (eventStartTimeInMinutes.toFloat() / totalMinutesInDay.toFloat()) * totalHeightPx
}




/// LES FONCTIONS ↓ SONT EN COMMENTAIRES CAR J'UTILISE CELLE DE MONTHLYTAB CAR ELLE SONT DISPONIBLE MEME DANS WEEKLYTAB ///
//@Composable
//fun W_EventListDialog(
//    selectedDate: LocalDate,
//    events: Map<LocalDate, List<Event>>,
//    onDismiss: () -> Unit,
//    onAddEventClick: () -> Unit
//) {
//    // Récupération des événements pour le jour sélectionné
//    val eventsForDay = events[selectedDate] ?: emptyList()
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Events for ${selectedDate.toString()}") },
//        text = {
//            Column {
//                LazyColumn {
//                    items(eventsForDay) { event ->
//                        // Affichage de chaque événement avec un encadré
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp)
//                                .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
//                            elevation = 4.dp
//                        ) {
//                            Column(modifier = Modifier.padding(16.dp)) {
//                                Text(text = " ${event.title}", style = MaterialTheme.typography.h6)
//                                Text(text = " ${event.description}", style = MaterialTheme.typography.body2)
//                                Text(text = " Start: ${event.startTime}") // Affichage direct de l'heure de début
//                                Text(text = " End: ${event.endTime}")   // Affichage direct de l'heure de fin
//                            }
//                        }
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = onAddEventClick) {
//                Text("Add Event")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Close")
//            }
//        }
//    )
//}
//
//
//@Composable
//fun W_EventDialog(
//    selectedDate: LocalDate,
//    onDismiss: () -> Unit,
//    onEventAdded: (Event) -> Unit
//) {
//    var eventTitle by remember { mutableStateOf("") }
//    var eventDescription by remember { mutableStateOf("") }
//    var eventStartTime by remember { mutableStateOf("") }
//    var eventEndTime by remember { mutableStateOf("") }
//    var expandedStartTime by remember { mutableStateOf(false) }
//    var expandedEndTime by remember { mutableStateOf(false) }
//
//    // Liste des heures disponibles (9 à 18 heures)
//    val availableHours = (1..24).map { "$it:00" }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(
//                text = "Add Event for ${selectedDate.toString()}",
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF2F4F4F),
//                fontSize = 18.sp
//            )
//        },
//        text = {
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                // Titre de l'événement
//                TextField(
//                    value = eventTitle,
//                    onValueChange = { eventTitle = it },
//                    label = { Text("Event Title") },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFE8F5E9))
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Description de l'événement
//                TextField(
//                    value = eventDescription,
//                    onValueChange = { eventDescription = it },
//                    label = { Text("Event Description") },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0xFFE8F5E9))
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Heure de début
//                Text("Event Start Time", fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { expandedStartTime = !expandedStartTime }
//                        .padding(8.dp)
//                        .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
//                ) {
//                    Text(
//                        text = eventStartTime.ifEmpty { "Select Start Time" },
//                        modifier = Modifier.padding(8.dp),
//                        color = Color.Black
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = expandedStartTime,
//                    onDismissRequest = { expandedStartTime = false }
//                ) {
//                    availableHours.forEach { hour ->
//                        DropdownMenuItem(onClick = {
//                            eventStartTime = hour
//                            expandedStartTime = false
//                        }) {
//                            Text(text = hour)
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Heure de fin
//                Text("Event End Time", fontWeight = FontWeight.Bold, fontSize = 14.sp)
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { expandedEndTime = !expandedEndTime }
//                        .padding(8.dp)
//                        .background(Color(0xFFB2DFDB), RoundedCornerShape(8.dp))
//                ) {
//                    Text(
//                        text = eventEndTime.ifEmpty { "Select End Time" },
//                        modifier = Modifier.padding(8.dp),
//                        color = Color.Black
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = expandedEndTime,
//                    onDismissRequest = { expandedEndTime = false }
//                ) {
//                    availableHours.forEach { hour ->
//                        DropdownMenuItem(onClick = {
//                            eventEndTime = hour
//                            expandedEndTime = false
//                        }) {
//                            Text(text = hour)
//                        }
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    val newEvent = Event(eventTitle, eventDescription, eventStartTime, eventEndTime)
//                    onEventAdded(newEvent)
//                    onDismiss()
//                },
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00796B))
//            ) {
//                Text("Add", color = Color.White)
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel", color = Color(0xFF00796B))
//            }
//        }
//    )
//}

//data class Event(
//    val title: String,
//    val description: String,
//    val startTime: String,
//    val endTime: String
//)


//@Composable
//fun W_ToDoListDialog(tasks: MutableList<String>, onDismiss: () -> Unit, context: Context) {
//    var newTask by remember { mutableStateOf("") }
//
//    // Charger l'état de la liste des tâches depuis SharedPreferences
//    val taskStates = rememberSaveable {
//        W_loadTaskStates(context, tasks.size)
//    }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("To Do List") },
//        text = {
//            Column {
//                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(tasks.size) { index ->
//                        // Passage de l'état de la tâche à W_ToDoItem
//                        W_ToDoItem(
//                            task = tasks[index],
//                            isChecked = taskStates[index],
//                            onCheckedChange = { isChecked ->
//                                taskStates[index] = isChecked
//                                W_saveTaskStates(context, taskStates)  // Sauvegarder l'état après changement
//                            }
//                        )
//                    }
//                }
//
//                // Champ pour ajouter une nouvelle tâche
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 8.dp)
//                ) {
//                    TextField(
//                        value = newTask,
//                        onValueChange = { newTask = it },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp),
//                        placeholder = { Text("New task") },
//                        singleLine = true,
//                        colors = TextFieldDefaults.textFieldColors(
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent
//                        )
//                    )
//                    Button(
//                        onClick = {
//                            if (newTask.isNotBlank()) {
//                                tasks.add(newTask)
//                                taskStates.add(false) // Ajout de l'état pour la nouvelle tâche
//                                newTask = ""
//                                W_saveTaskStates(context, taskStates)  // Sauvegarder après ajout
//                            }
//                        },
//                        modifier = Modifier.padding(start = 8.dp)
//                    ) {
//                        Text("Add")
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = onDismiss
//            ) {
//                Text("Close")
//            }
//        }
//    )
//}
//
//// Fonction pour sauvegarder l'état des tâches dans SharedPreferences
//fun W_saveTaskStates(context: Context, taskStates: List<Boolean>) {
//    val sharedPreferences = context.getSharedPreferences("todo_preferences", Context.MODE_PRIVATE)
//    val editor = sharedPreferences.edit()
//    // Sauvegarde sous forme de chaîne, avec chaque valeur séparée par une virgule
//    editor.putString("task_states", taskStates.joinToString(","))
//    editor.apply()
//}
//
//// Fonction pour charger l'état des tâches depuis SharedPreferences
//fun W_loadTaskStates(context: Context, taskCount: Int): MutableList<Boolean> {
//    val sharedPreferences = context.getSharedPreferences("todo_preferences", Context.MODE_PRIVATE)
//    val savedState = sharedPreferences.getString("task_states", "")
//    val states = savedState?.split(",")?.map { it.toBoolean() }?.toMutableList() ?: mutableListOf()
//    // Remplir la liste avec des valeurs par défaut si l'état est incomplet
//    while (states.size < taskCount) {
//        states.add(false)
//    }
//    return states
//}
//
//@Composable
//fun W_ToDoItem(task: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFFB0C4DE), RoundedCornerShape(8.dp))
//            .padding(8.dp)
//    ) {
//        Checkbox(
//            checked = isChecked,
//            onCheckedChange = { onCheckedChange(it) }
//        )
//        Text(
//            text = task,
//            color = if (isChecked) Color.Gray else Color.Black,  // Changer la couleur si cochée
//            modifier = Modifier
//                .padding(start = 8.dp)
//                .graphicsLayer(alpha = if (isChecked) 0.5f else 1f)  // Utiliser graphicsLayer pour l'opacité
//        )
//    }
//}




