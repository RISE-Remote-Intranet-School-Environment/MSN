package org.schoolapp.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility

// Data class for Tutor
data class Tutor(
    val name: String,
    val rating: Int,
    val courses: String,
    val description: String // Nouveau champ pour la description
)

@Composable
fun PrivateLessonsTab() {
    val tutors = listOf(
        Tutor("Samir", 4, "Math", "Samir est un professeur expérimenté en Mathématiques, passionné par l'enseignement des concepts complexes de manière simple."),
        Tutor("Haitam", 5, "Python", "Haitam est un expert en Python avec plus de 10 ans d'expérience en développement et enseignement."),
        Tutor("Yllke", 4, "Java", "Yllke est une passionnée de Java qui aime partager ses connaissances avec ses étudiants."),
        Tutor("Ibrahim", 3, "English", "Ibrahim est un enseignant dévoué en Anglais, spécialisé dans l'amélioration des compétences orales."),
        Tutor("Ayoub", 5, "Rust", "Ayoub est un spécialiste de Rust, enthousiaste à l'idée d'enseigner ce langage moderne et performant.")
    )

    // Map des états de visibilité pour chaque tuteur
    val descriptionVisibilityStates = remember { mutableStateOf(tutors.associate { it.name to false }) }
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Titre de la page
        Text(
            text = "Private Lessons",
            style = MaterialTheme.typography.h6.copy(fontSize = 20.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barre de recherche
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search for tutors...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Liste des tuteurs filtrés par la recherche
        val filteredTutors = tutors.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(filteredTutors) { tutor ->
                // Passez l'état actuel et le callback pour chaque tuteur
                TutorItem(
                    tutor = tutor,
                    isDescriptionVisible = descriptionVisibilityStates.value[tutor.name] == true,
                    onToggleDescription = {
                        // Inverser l'état de visibilité pour le tuteur correspondant
                        descriptionVisibilityStates.value = descriptionVisibilityStates.value.toMutableMap().apply {
                            this[tutor.name] = !(this[tutor.name] ?: false)
                        }
                    }
                )
                Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun TutorItem(
    tutor: Tutor,
    isDescriptionVisible: Boolean,
    onToggleDescription: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onToggleDescription() }
        ) {
            // Avatar Image
            Image(
                painter = painterResource(id = R.drawable.ok),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            // Tutor details: Name, Courses, and Stars
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tutor.name,
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Courses: ${tutor.courses}",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
                RatingBar(rating = tutor.rating)
            }

            // Bouton pour basculer la visibilité
            IconButton(
                onClick = { onToggleDescription() },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Toggle description",
                    tint = Color.Gray
                )
            }
        }

        // Description text (avec animation)
        AnimatedVisibility(visible = isDescriptionVisible) {
            Text(
                text = tutor.description, // Utilise la description personnalisée
                style = MaterialTheme.typography.body2.copy(color = Color.DarkGray),
                modifier = Modifier.padding(start = 64.dp, top = 8.dp)
            )
        }
    }
}

@Composable
fun RatingBar2(rating: Int) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < rating) Color.Yellow else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
