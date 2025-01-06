package org.schoolapp.project


import androidx.compose.ui.graphics.Color
import java.time.LocalDate

/*/ Classe pour représenter un événement
data class Event(
    val title: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val color: Color
)

// Fonction pour générer les données des événements
fun getEventsData(): MutableMap<LocalDate, MutableList<Event>> {
    return generateSequence(LocalDate.of(2024, 11, 1)) { it.plusDays(1) }
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
*/