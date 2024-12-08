package org.schoolapp.project

import DocsTab
import RecentgradesTab
import ReportTab
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter



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
                    "Classes" -> ClassesView(onNavigateBack = { currentScreen = "Home" }, onNavigateToProfessors = { currentScreen = "Professors" })
                    "Professors" -> ProfessorsScreen(onNavigateBack = { currentScreen = "Classes" })
                }
            }
        )
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSection(
                icon = painterResource(id = R.drawable.calendar), // Remplacez par vos images
                label = "Calendar",
                onClick = { onNavigate("Calendar") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.klass), // Remplacez par vos images
                label = "Classes",
                onClick = { onNavigate("Classes") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSection(
                icon = painterResource(id = R.drawable.collaboration), // Remplacez par vos images
                label = "Collaboration",
                onClick = { onNavigate("Collaboration") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.grades), // Remplacez par vos images
                label = "Grades",
                onClick = { onNavigate("Grades") }
            )
        }
    }
}

@Composable
fun HomeSection(icon: Painter, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = icon,
            contentDescription = label,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(label)
    }
}

//@Composable
//fun HomeScreen(onNavigate: (String) -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(onClick = { onNavigate("Calendar") }) {
//            Text("Calendar")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate("Classes") }) {
//            Text("Classes")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate("Collaboration") }) {
//            Text("Collaboration")
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = { onNavigate("Grades") }) {
//            Text("Grades")
//        }
//    }
//}


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
fun ClassesView(onNavigateBack: () -> Unit, onNavigateToProfessors: () -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filteredCourses by remember { mutableStateOf(listOf("Data Science", "History", "Science")) }

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
                    text = "Available courses:",
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
                    onClick = onNavigateToProfessors,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("See the professors")
                }
            }
        }
    )
}

data class Professor(val name: String, val courseCount: Int, val image: Int, val rating: Int)

@Composable
fun ProfessorsScreen(onNavigateBack: () -> Unit) {
    val professors = listOf(
        Professor("Mme Dupont", 3, R.drawable.okok, 4),
        Professor("M. Martin", 5, R.drawable.okok, 5),
        Professor("Mme Durand", 4, R.drawable.okok, 3),
        Professor("M. Lefevre", 2, R.drawable.okok, 4),
        Professor("Mme Moreau", 6, R.drawable.okok, 5)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Professors") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Classes"
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Professors List:",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ProfessorList(professors = professors)
            }
        }
    )
}

@Composable
fun ProfessorList(professors: List<Professor>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(professors) { professor ->
            ProfessorItem(professor = professor)
        }
    }
}

@Composable
fun ProfessorItem(professor: Professor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = professor.image),
            contentDescription = professor.name,
            modifier = Modifier
                .size(60.dp)
                .padding(end = 16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = professor.name,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "Courses: ${professor.courseCount}",
                style = MaterialTheme.typography.body2
            )
            RatingBar2(rating = professor.rating)
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "More details"
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
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ok),
                contentDescription = "Course Image",
                modifier = Modifier.size(60.dp).padding(end = 16.dp)
            )
            Column {
                Text(text = courseName, style = MaterialTheme.typography.subtitle1)
                Text(text = teacherName, style = MaterialTheme.typography.body2)
            }
        }
        IconButton(onClick = { /* Show course details */ }) {
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Course details")
        }
    }
}

@Composable
fun RatingBar(rating: Int) {
    Row(
        modifier = Modifier.padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val icon = if (index < rating) Icons.Default.Star else Icons.Default.StarBorder
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


