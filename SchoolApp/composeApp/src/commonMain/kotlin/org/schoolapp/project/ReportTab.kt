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
    val subject: String,
    val ects: Int,
    val grade: Int,
    val total: Int
)

@Composable
fun ReportTab() {
    val reportItems = listOf(
        ReportItem("Physics", 5, 13, 20),
        ReportItem("Chemistry", 3, 12, 20),
        ReportItem("Law", 7, 10, 20),
        ReportItem("Thermodynamics", 4, 8, 20)
    )

    val average = reportItems.map { it.grade.toDouble() }.average()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB)) // Light blue background
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Report Card:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Report Card Table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            backgroundColor = Color(0xFFB0C4DE), // Grayish background for the table
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                reportItems.forEach { item ->
                    ReportCardRow(item)
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Average Grade
        // Average Grade
        Text(
            text = "Average: 11/20",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun ReportCardRow(item: ReportItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Subject Name
        Text(
            text = item.subject,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        // ECTS Points
        Text(
            text = "${item.ects} ECTS",
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = Color.Gray
        )

        // Grade
        Text(
            text = item.grade.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (item.grade >= item.total / 2) Color(0xFF228B22) else Color.Red // Green if passing, red if failing
        )

        // Total Score
        Text(
            text = "/ ${item.total}",
            modifier = Modifier.weight(0.5f),
            fontSize = 16.sp
        )
    }
}
