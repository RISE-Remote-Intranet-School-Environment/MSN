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
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable



@Composable
fun WeeklyTab() {
    var currentWeekStart by remember { mutableStateOf(LocalDate.now().with(DayOfWeek.MONDAY)) }
    var currentDay by remember { mutableStateOf(LocalDate.now().dayOfMonth) }
    var events by remember { mutableStateOf(mutableMapOf<LocalDate, MutableList<Event>>()) }

    LaunchedEffect(Unit) {
        events = generateSequence(LocalDate.of(2024, 11, 1)) { it.plusDays(1) }
            .takeWhile { it.isBefore(LocalDate.of(2026, 1, 1)) }
            .associate { date ->
                date to mutableListOf(
                    Event(
                        title = "Control Theory",
                        description = "Au 2F10 avec DBR",
                        startTime = "08h00",
                        endTime = "12h00",
                        color = Color(0xFFFFA500) // Orange
                    ),
                    Event(
                        title = "Software Architecture",
                        description = "Au 1E04 avec J3L",
                        startTime = "13h00",
                        endTime = "16h00",
                        color = Color(0xFF87CEFA) // Bleu clair
                    ),
                    Event(
                        title = "Network Concepts",
                        description = "Au 2D52 avec DSM",
                        startTime = "17h00",
                        endTime = "19h00",
                        color = Color(0xFF32CD32) // Vert lime
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
fun W_CalendarGrid(
    currentWeekStart: LocalDate,
    currentDay: Int,
//    events: MutableMap<LocalDate, List<Event>>,
    events: MutableMap<LocalDate, MutableList<Event>>,
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

                        // Calculer les chevauchements des événements
                        val dayEvents = events[date] ?: emptyList()
                        val overlappingEvents = calculateOverlappingEvents(dayEvents)

                        // Affichage des événements pour chaque date
                        overlappingEvents.forEach { group ->
                            val groupSize = group.size

                            group.forEachIndexed { index, event ->
                                val startOffsetPx = timeToOffset(event.startTime, totalHeightPx) + 30f
                                val endOffsetPx = timeToOffset(event.endTime, totalHeightPx) + 50f
                                val eventHeightPx = endOffsetPx - startOffsetPx

                                // Conversion des pixels en Dp
                                val startOffset = startOffsetPx / density.density
                                val eventHeight = eventHeightPx / density.density

                                // Calcul de la largeur ajustée en fonction du nombre d’événements dans le groupe
                                val eventWidth = (maxWidth / groupSize)
                                val horizontalOffsetDp = eventWidth * index

                                // Placement de l’événement dans le calendrier
                                Box(
                                    modifier = Modifier
                                        .width(eventWidth) // Largeur proportionnelle au groupe
                                        .offset(x = horizontalOffsetDp, y = startOffset.dp) // Offset horizontal et vertical
                                        .height(eventHeight.dp) // Hauteur de l’événement
                                        .background(
                                            color = event.color,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(4.dp)
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
}

fun calculateOverlappingEvents(events: List<Event>): List<List<Event>> {
    val sortedEvents = events.sortedBy { it.startTime }
    val overlappingGroups = mutableListOf<MutableList<Event>>()

    sortedEvents.forEach { event ->
        val addedToGroup = overlappingGroups.any { group ->
            val lastEventInGroup = group.last()
            val isOverlapping =
                event.startTime < lastEventInGroup.endTime && event.endTime > lastEventInGroup.startTime

            if (isOverlapping) {
                group.add(event)
            }
            isOverlapping
        }

        if (!addedToGroup) {
            overlappingGroups.add(mutableListOf(event))
        }
    }

    return overlappingGroups
}


fun timeToOffset(time: String, totalHeightPx: Float): Float {
    // Suppression du caractère 'h' et séparation de l'heure et des minutes
    val (hour, minute) = time.split("h").map { it.toInt() }

    val totalMinutesInDay = 24 * 60
    val eventStartTimeInMinutes = hour * 60 + minute

    // Calcul du décalage proportionnel en fonction du totalHeightPx (en pixels)
    return (eventStartTimeInMinutes.toFloat() / totalMinutesInDay.toFloat()) * totalHeightPx
}




