package org.schoolapp.project

//import DocsTab
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send


import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Description
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
    var enrolledCourses by remember { mutableStateOf(mutableListOf<String>()) } // Liste des cours inscrits
    var selectedCourse by remember { mutableStateOf<String?>(null) }

    var selectedProfessor by remember { mutableStateOf<Professor?>(null) }

    // Fonction de désinscription
    val onUnenroll: (String) -> Unit = { course ->
        enrolledCourses = enrolledCourses.filter { it != course }.toMutableList() // Mise à jour de l'état
    }

    // Fonction pour gérer l'inscription d'un cours
    val onEnroll: (String) -> Unit = { course ->
        // Ajouter le cours à la liste si ce n'est pas déjà inscrit
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
                    "Collaboration" -> CollaborationView(onNavigateBack = { currentScreen = "Home" })
                    "Calendar" -> CalendarView(onNavigateBack = { currentScreen = "Home" })
                    "Grades" -> GradesView(onNavigateBack = { currentScreen = "Home" })
                    "Classes" -> ClassesView(
                        onNavigateBack = { currentScreen = "Home" },
                        onNavigateToProfessors = { currentScreen = "Professors" },
                        onNavigateToMyCourses = { currentScreen = "MyCourses" },
                        enrolledCourses = enrolledCourses,
                        onEnroll = onEnroll // Passer la fonction d'inscription
                    )
                    "Professors" -> ProfessorsScreen(
                        onNavigateBack = { currentScreen = "Classes" },
                        onNavigateToProfessorDetails = { professor ->
                            selectedProfessor = professor
                            currentScreen = "ProfessorDetails"
                        }
                    )
                    "ProfessorDetails" -> {
                        selectedProfessor?.let {
                            ProfessorDetailsScreen(
                                professor = it,
                                onNavigateBack = { currentScreen = "Professors" }
                            )
                        }
                    }
                    "MyCourses" -> {
                        MyCoursesScreen(
                            enrolledCourses = enrolledCourses,
                            onNavigateBack = { currentScreen = "Classes" },
                            onUnenroll = onUnenroll,
                            onAccessDetails = { course ->
                                selectedCourse = course
                                currentScreen = "CourseDetails"
                            }
                        )
                    }
                    "CourseDetails" -> {
                        selectedCourse?.let { courseName ->
                            CourseDetailsPage(
                                courseName = courseName,
                                onNavigateBack = { currentScreen = "MyCourses" }
                            )
                        }
                    }
                    "Classes" -> ClassesView(
                        onNavigateBack = { currentScreen = "Home" },
                        onNavigateToProfessors = { currentScreen = "Professors" },
                        onNavigateToMyCourses = { currentScreen = "MyCourses" },
                        enrolledCourses = enrolledCourses,
                        onEnroll = onEnroll
                    )
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
                icon = painterResource(id = R.drawable.calendar),
                label = "Calendar",
                onClick = { onNavigate("Calendar") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.klass),
                label = "Classes",
                onClick = { onNavigate("Classes") }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HomeSection(
                icon = painterResource(id = R.drawable.collaboration),
                label = "Collaboration",
                onClick = { onNavigate("Collaboration") }
            )
            HomeSection(
                icon = painterResource(id = R.drawable.grades),
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
fun EnrolledCoursesList(enrolledCourses: List<String>) {
    LazyColumn {
        items(enrolledCourses) { course ->
            Text(
                text = "Enrolled course: $course",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MyCoursesScreen(
    enrolledCourses: List<String>,
    onNavigateBack: () -> Unit,
    onUnenroll: (String) -> Unit,
    onAccessDetails: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
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
                    text = "Your Enrolled Courses:",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (enrolledCourses.isEmpty()) {
                    Text("You are not enrolled in any courses yet.", style = MaterialTheme.typography.body1)
                } else {
                    LazyColumn {
                        items(enrolledCourses) { course ->
                            CourseItem(
                                courseName = course,
                                onUnenroll = { onUnenroll(course) },
                                onAccessDetails = { onAccessDetails(course) }
                            )
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun CourseItem(courseName: String, onUnenroll: () -> Unit, onAccessDetails: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
            )
            Row {
                IconButton(onClick = onAccessDetails) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Access details of $courseName",
                        tint = MaterialTheme.colors.primary
                    )
                }
                IconButton(onClick = onUnenroll) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Unenroll from $courseName",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}



@Composable
fun ClassesView(
    onNavigateBack: () -> Unit,
    onNavigateToProfessors: () -> Unit,
    onNavigateToMyCourses: () -> Unit,
    enrolledCourses: MutableList<String>,
    onEnroll: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val availableCourses = listOf("Data Science", "History", "Science")
    val filteredCourses = availableCourses.filter { it.contains(searchQuery.text, ignoreCase = true) }
    val buttonStates = remember { mutableStateMapOf<String, Boolean>() } // Utiliser mutableStateMapOf

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
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text("Search courses") }
                )

                filteredCourses.forEach { course ->
                    ClassItem(
                        courseName = course,
                        teacherName = "Teacher Name",
                        isRegistered = buttonStates[course] ?: false,
                        onEnroll = {
                            // Mettre à jour l'état lorsque le bouton est cliqué
                            buttonStates[course] = true
                            onEnroll(course)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToProfessors,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("See the professors")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onNavigateToMyCourses,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("See my courses")
                }
            }
        }
    )
}







data class Professor(
    val name: String,
    val courseCount: Int,
    val image: Int,
    val email: String,    // Ajout de l'email
    val trigram: String,  // Ajout du trigramme
    val course: String    // Ajout du cours que le professeur enseigne
)


@Composable
fun ProfessorsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToProfessorDetails: (Professor) -> Unit
) {
    val professors = listOf(
        Professor("Mme Dupont", 3, R.drawable.okok,  "mme.dupont@mail.com", "DUP", "Data Science"),
        Professor("M. Martin", 5, R.drawable.okok, "m.martin@mail.com", "MAR", "History"),
        Professor("Mme Durand", 4, R.drawable.okok,  "mme.durand@mail.com", "DUR", "Science"),
        Professor("M. Lefevre", 2, R.drawable.okok,  "m.lefevre@mail.com", "LEF", "Mathematics"),
        Professor("Mme Moreau", 6, R.drawable.okok,  "mme.moreau@mail.com", "MOR", "Physics")
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

                ProfessorList(
                    professors = professors,
                    onNavigateToProfessorDetails = onNavigateToProfessorDetails
                )
            }
        }
    )
}


@Composable
fun ProfessorList(
    professors: List<Professor>,
    onNavigateToProfessorDetails: (Professor) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(professors) { professor ->
            ProfessorItem(professor = professor, onClick = { onNavigateToProfessorDetails(professor) })
        }
    }
}


@Composable
fun ProfessorItem(professor: Professor, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(80.dp)
            .clickable { onClick() },
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
        }

        IconButton(onClick = { onClick() }) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "More details"
            )
        }
    }
}

@Composable
fun ProfessorDetailsScreen(professor: Professor, onNavigateBack: () -> Unit) {
    var showConversation by remember { mutableStateOf(false) }

    if (showConversation) {
        ConversationScreen(professor = professor, onNavigateBack = { showConversation = false })
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(professor.name) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to Professors"
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
                        text = "Professor Details",
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Name: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.name)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Email: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.email)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Course: ",
                                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(text = professor.course)

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { showConversation = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Contact Professor")
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun CourseDetailsPage(courseName: String, onNavigateBack: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Details", "Files")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Course: $courseName") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back to My Courses"
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
                0 -> CourseDetailsTab(courseName)
                1 -> CourseFilesTab(courseName)
            }
        }
    )
}

val courseFiles = mapOf(
    "Data Science" to listOf(
        "Introduction_to_Data_Science.pdf",
        "Machine_Learning_Basics.pptx",
        "Python_for_Data_Analysis.docx"
    ),
    "History" to listOf(
        "Ancient_Civilizations.pdf",
        "World_War_II_Overview.pptx",
        "Modern_History_Notes.docx"
    ),
    "Science" to listOf(
        "Basic_Physics_Concepts.pdf",
        "Introduction_to_Biology.pptx",
        "Chemistry_Experiments.docx"
    )
)

@Composable
fun CourseFilesTab(courseName: String) {
    val files = courseFiles[courseName] ?: listOf("No files available for this course.")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Files for $courseName",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(files) { fileName ->
                FileItem(fileName)
            }
        }
    }
}

@Composable
fun FileItem(fileName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = "File Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = fileName,
                style = MaterialTheme.typography.body1
            )
        }
    }
}




@Composable
fun ClassItem(courseName: String, teacherName: String, isRegistered: Boolean, onEnroll: () -> Unit) {
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
        Button(
            onClick = onEnroll,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(if (isRegistered) "Registered" else "Register")
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


val courseDetails = mapOf(
    "Data Science" to """
        This course provides an extensive overview of data analysis, machine learning, and visualization techniques. 
        You will learn to work with tools such as Python, R, and SQL to manipulate and analyze large datasets.
        The course also includes topics like data wrangling, predictive modeling, and statistical inference.
        By the end, you will complete a capstone project to apply these concepts in real-world scenarios.
    """.trimIndent(),
    "History" to """
        Dive into the fascinating world of history with this comprehensive course. 
        Explore key historical events, such as the Industrial Revolution, the World Wars, and the rise and fall of civilizations. 
        We also focus on historical methodology, helping you understand how historians analyze sources and construct narratives.
        This course includes group discussions, document analysis, and essays to refine your critical thinking skills.
    """.trimIndent(),
    "Science" to """
        This foundational course explores core concepts in physics, chemistry, and biology. 
        You will conduct experiments to understand laws like Newton's mechanics, chemical reactions, and the process of photosynthesis.
        Additionally, we cover contemporary topics like climate change, genetic engineering, and renewable energy.
        The course encourages hands-on learning and includes lab sessions to deepen your understanding.
    """.trimIndent()
)


@Composable
fun CourseDetailsTab(courseName: String) {
    val details = courseDetails[courseName] ?: "Details for this course are not available."

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Course Details for $courseName",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Overview:",
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = details,
            style = MaterialTheme.typography.body1,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "What You Will Learn:",
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "1. Key topics in ${courseName.lowercase()}.\n2. Hands-on projects.\n3. Critical thinking and analysis skills.",
            style = MaterialTheme.typography.body1,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ConversationScreen(
    professor: Professor,
    onNavigateBack: () -> Unit
) {
    // Liste des messages simulés
    val messages = listOf(
        "Hello, Professor ${professor.name}. I have a question about the ${professor.course} course.",
        "Sure, what is your question?",
        "I’m struggling with the last assignment. Could you provide some clarification?",
        "Of course! Let me explain it step by step."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat with ${professor.name}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message, isUser = messages.indexOf(message) % 2 == 0)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                MessageInputBox()
            }
        }
    )
}

@Composable
fun ChatBubble(message: String, isUser: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            backgroundColor = if (isUser) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            modifier = Modifier
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.body1,
                color = if (isUser) Color.White else Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MessageInputBox() {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") }
        )
        IconButton(onClick = {
            // Logique pour envoyer un message (ici on peut juste réinitialiser le texte pour simuler l'envoi)
            text = ""
        }) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Message",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}




















//Tu preferes moi ou ton pere ?