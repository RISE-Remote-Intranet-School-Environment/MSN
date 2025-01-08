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
fun DailyTab() {
    var currentDay by remember { mutableStateOf(LocalDate.now()) }
    var events by remember { mutableStateOf(generateEvents()) }

    val tasks = remember { mutableStateListOf(*generateToDoItems().toTypedArray()) }
    var showEventDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showToDoListDialog by remember { mutableStateOf(false) }
    var showEventListDialog by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(Color(0xFF5F9EA0))
            .background(Color(0xFFFFFFFF))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
                .background(Color(0xFF2F4F4F), RoundedCornerShape(12.dp))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { currentDay = currentDay.minusDays(1) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous Day",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = currentDay.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    IconButton(onClick = { currentDay = currentDay.plusDays(1) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next Day",
                            tint = Color.White
                        )
                    }
                }

                // Grille quotidienne
                D_CalendarGrid(
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
fun D_CalendarGrid(
    currentDay: LocalDate,
    events: MutableMap<LocalDate, MutableList<Event>>,
    onDayClick: (LocalDate) -> Unit
) {
    val scrollState = rememberScrollState() // État de défilement vertical partagé
    val hours = (0..24).toList() // De 0h à 24h

    val hourHeightDp = 1440.dp / 24 // Hauteur de chaque heure en dp

    val dailyDays = listOf(currentDay) // Liste avec un seul jour: le jour actuel
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Column(Modifier.fillMaxSize()) {
        // En-tête du jour
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(0.15f))
            dailyDays.forEachIndexed { index, date ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(
                            color = Color(0x80D3D3D3),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onDayClick(date) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = daysOfWeek[date.dayOfWeek.ordinal],
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

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
                            .height(hourHeightDp)
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                    )
                }
            }

            // Colonnes des événements
            Row(
                modifier = Modifier.weight(0.85f)
            ) {
                dailyDays.forEach { date ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                            .height(1440.dp) // Hauteur totale pour 24 heures
                    ) {
                        val density = LocalDensity.current // Récupération du contexte de densité
                        val totalHeightPx = maxHeight.value * density.density // Conversion de Dp en pixels

                        // Filtrer les événements visibles
                        val dayEvents = events[currentDay]?.filter { it.isVisible } ?: emptyList()
                        val overlappingEvents = calculateOverlappingEvents(dayEvents)

                        overlappingEvents.forEach { group ->
                            val groupSize = group.size
                            group.forEachIndexed { index, event ->
                                val startOffsetPx = timeToOffset(event.startTime, totalHeightPx) + 30f
                                val endOffsetPx = timeToOffset(event.endTime, totalHeightPx) + 50f
                                val eventHeightPx = endOffsetPx - startOffsetPx

                                val startOffset = startOffsetPx / density.density
                                val eventHeight = eventHeightPx / density.density

                                val eventWidth = (maxWidth / groupSize)
                                val horizontalOffsetDp = eventWidth * index

                                Box(
                                    modifier = Modifier
                                        .width(eventWidth)
                                        .offset(
                                            x = horizontalOffsetDp,
                                            y = startOffset.dp
                                        )
                                        .height(eventHeight.dp)
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







