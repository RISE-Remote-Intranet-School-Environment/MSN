package org.schoolapp.project

import androidx.compose.material.*
import androidx.compose.runtime.*

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf("Home") }
    var enrolledCourses by remember { mutableStateOf(mutableListOf<String>()) } // Liste des cours inscrits
    var selectedCourse by remember { mutableStateOf<String?>(null) }

    var selectedProfessor by remember { mutableStateOf<Professor?>(null) }

    val onUnenroll: (String) -> Unit = { course ->
        enrolledCourses = enrolledCourses.filter { it != course }.toMutableList() // Mise à jour de l'état
    }

    val onEnroll: (String) -> Unit = { course ->
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course)
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("School App") })
            },
            content = {
                when (currentScreen) {
                    "Home" -> HomeScreen(onNavigate = { screen -> currentScreen = screen })
                    "Calendar" -> CalendarView(onNavigateBack = { currentScreen = "Home" })
                    "Collaboration" -> CollaborationView(onNavigateBack = { currentScreen = "Home" })
                    "Grades" -> GradesView(onNavigateBack = { currentScreen = "Home" })
                    "Classes" -> ClassesView(
                        onNavigateBack = { currentScreen = "Home" },
                        onNavigateToProfessors = { currentScreen = "Professors" },
                        onNavigateToMyCourses = { currentScreen = "MyCourses" },
                        enrolledCourses = enrolledCourses,
                        onEnroll = onEnroll
                    )
                    "Professors" -> ProfessorsScreen(
                        onNavigateBack = { currentScreen = "Classes" },
                        onNavigateToProfessorDetails = { professor ->
                            selectedProfessor = professor
                            currentScreen = "ProfessorDetails"
                        }
                    )
                    "ProfessorDetails" -> selectedProfessor?.let { professor ->
                        ProfessorDetailsScreen(
                            professor = professor,
                            onNavigateBack = { currentScreen = "Professors" }
                        )
                    }

                    "MyCourses" -> MyCoursesScreen(
                        enrolledCourses = enrolledCourses,
                        onNavigateBack = { currentScreen = "Classes" },
                        onUnenroll = onUnenroll,
                        onAccessDetails = { course ->
                            selectedCourse = course
                            currentScreen = "CourseDetails"
                        }
                    )
                    "CourseDetails" -> selectedCourse?.let { courseName ->
                        CourseDetailsPage(
                            courseName = courseName,
                            onNavigateBack = { currentScreen = "MyCourses" }
                        )
                    }
                }
            }
        )
    }
}




























