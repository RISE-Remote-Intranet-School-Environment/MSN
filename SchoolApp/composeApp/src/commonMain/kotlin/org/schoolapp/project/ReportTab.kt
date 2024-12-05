import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ReportItem(
    val trigram: String,
    val subject: String,
    val ects: Int,
    val grade: Int,
    val total: Int
)

@Composable
fun ReportTab() {
    val recentGrades = listOf(
        Grade("PHY", "Labo 2 : Optic", 14, 20),
        Grade("CHE", "Report Oxydo-reduction", 8, 10),
        Grade("LAW", "Essay Constitution", 10, 30),
        Grade("THE", "Exercices : Bernouilli", 4, 15)
    )

    val groupedGrades = recentGrades.groupBy { it.subject }
    val reportItems = groupedGrades.map { (trigram, grades) ->
        val averageGrade = grades.map { it.score.toDouble() / it.total * 20 }.average()
        val subjectName = when (trigram) {
            "PHY" -> "Physics"
            "CHE" -> "Chemistry"
            "LAW" -> "Law"
            "THE" -> "Thermodynamics"
            else -> "Unknown"
        }
        val ects = when (trigram) {
            "PHY" -> 5
            "CHE" -> 3
            "LAW" -> 7
            "THE" -> 4
            else -> 0
        }
        ReportItem(trigram, subjectName, ects, averageGrade.toInt(), 20)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Report Card:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Report Card Table
        Card(
            modifier = Modifier
                .fillMaxWidth(1f) // Adjust the width fraction here
                .padding(horizontal = 16.dp, vertical = 8.dp),
            backgroundColor = Color(0xFFB0C4DE),
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                reportItems.forEach { item ->
                    ReportCardRow(item)
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average Grade
        val overallAverage = reportItems.map { it.grade.toDouble() }.average()
        Text(
            text = "Average: ${overallAverage.toInt()}/20",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun ReportCardRow(item: ReportItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Subject Trigram and Name
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = item.trigram,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.subject,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ECTS Points, Grade, and Total Score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${item.ects} ECTS",
                fontSize = 16.sp,
                color = Color.Blue
            )
            Row {
                Text(
                    text = item.grade.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.grade >= item.total / 2) Color(0xFF228B22) else Color.Red
                )
                Text(
                    text = "/ ${item.total}",
                    fontSize = 16.sp
                )
            }
        }
    }
}