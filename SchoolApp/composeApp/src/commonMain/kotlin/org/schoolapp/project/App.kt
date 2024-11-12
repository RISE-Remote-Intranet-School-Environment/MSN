package org.schoolapp.project

import DocsTab
import ReportTab
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.TextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder




@Composable
fun App() {
    var currentScreen by remember { mutableStateOf("Home") }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("School App") })
            },
            content = {
                when (currentScreen) {
                    "Home" -> HomeScreen(onNavigate = { screen -> currentScreen = screen })
                    "Collaboration" -> CollaborationView(onNavigateBack = { currentScreen = "Home" })
                    "Calendar" -> CalendarView(onNavigateBack = { currentScreen = "Home" })
                    "Grades" -> GradesView(onNavigateBack = { currentScreen = "Home" })
                    "Classes" -> ClassesView(onNavigateBack = { currentScreen = "Home" })
                }
            }
        )
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { onNavigate("Calendar") }) {
            Text("Calendar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onNavigate("Classes") }) {
            Text("Classes")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onNavigate("Collaboration") }) {
            Text("Collaboration")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onNavigate("Grades") }) {
            Text("Grades")
        }
    }
}

@Composable
fun CalendarView(onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Monthly", "Weekly", "Daily")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Calendar") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home"
                            )
                        }
                    }
                )

                // TabRow at the top of the screen
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        content = {
            when (selectedTab) {
                0 -> MonthlyTab()
                1 -> WeeklyTab()
                2 -> DailyTab()
            }
        }
    )
}

@Composable
fun CollaborationView(onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Forum", "Sales", "Docs", "Private Lessons")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Collaboration") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home"
                            )
                        }
                    }
                )

                // TabRow at the top of the screen
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        content = {
            when (selectedTab) {
                0 -> ForumTab()
                1 -> SalesTab()
                2 -> DocsTab()
                3 -> PrivateLessonsTab()
            }
        }
    )
}

@Composable
fun GradesView(onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Recent Grades", "Simulation", "Report Card")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Grades") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Home"
                            )
                        }
                    }
                )

                // TabRow at the top of the screen
                TabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        content = {
            when (selectedTab) {
                0 -> RecentgradesTab()
                1 -> SimulationgradesTab()
                2 -> ReportTab()
            }
        }
    )
}

@Composable
fun ClassesView(onNavigateBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourses by remember { mutableStateOf(listOf("Data Science", "History", "Science")) }

    val professorsCourses = remember {
        mapOf(
            "Mme Dupont" to listOf("Data Science", "History"),
            "M. Martin" to listOf("Mathematics", "Physics"),
            "Mme Durand" to listOf("Biology", "Chemistry")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Home"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Available courses :",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        filteredCourses = listOf("Data Science", "History", "Science").filter {
                            it.contains(searchQuery.text, ignoreCase = true)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(1.dp, MaterialTheme.colors.primary)
                        .padding(8.dp),
                    label = { Text("Search courses") }
                )

                filteredCourses.forEach { course ->
                    ClassItem(courseName = course, teacherName = "Teacher Name")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Action du bouton */ },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("See the program")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Professors :",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                professorsCourses.forEach { (professorName, courses) ->
                    ProfessorItem(professorName = professorName, numberOfCourses = courses.size)
                }
            }
        }
    )
}

@Composable
fun ProfessorItem(professorName: String, numberOfCourses: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = professorName,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "Courses: $numberOfCourses",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun ClassItem(courseName: String, teacherName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = courseName, style = MaterialTheme.typography.subtitle1)
            Text(text = teacherName, style = MaterialTheme.typography.body2)
        }
        IconButton(onClick = { /* Show course details */ }) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Course details")
        }
    }
}

@Composable
fun AddCourseButton() {
    Button(
        onClick = { /* Handle add course action */ },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Add a new course")
    }
}

@Composable
fun SortCoursesButton() {
    Button(
        onClick = { /* Handle sort action */ },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Sort courses")
    }
}

@Composable
fun SeeCourseDetailsButton() {
    Button(
        onClick = { /* Navigate to course details page */ },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("See course details")
    }
}

