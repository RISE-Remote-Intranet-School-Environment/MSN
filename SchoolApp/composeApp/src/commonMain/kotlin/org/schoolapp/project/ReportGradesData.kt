package org.schoolapp.project

data class Grade(
    val subject: String,
    val assessment: String,
    val ratio: Int,
    val score: Int,
    val total: Int=20,
)
data class ReportItem(
    val trigram: String,
    val subject: String,
    val ects: Int,
    val grade: Int,
    val total: Int = 20
)


// Example grades (this will be in the data layer)
val recentGrades = listOf(
    Grade("PHY", "Labo 2 : Optic", 14, 20),
    Grade("CHE", "Report Oxydo-reduction", 8, 10),
    Grade("LAW", "Essay Constitution", 10, 30),
    Grade("THE", "Exercices : Bernouilli", 4, 15)
)

val grades = listOf(
    Grade("PHY", "Labo 2 : Optic", 14, 20),
    Grade("CHE", "Report Oxydo-reduction", 8, 10),
    Grade("LAW", "Essay Constitution", 10, 8),
    Grade("THE", "Exercices : Bernouilli", 4, 15)
)

// Function to group and calculate report items
fun getGroupedGrades(): List<ReportItem> {
    val groupedGrades = recentGrades.groupBy { it.subject }
    return groupedGrades.map { (trigram, grades) ->
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
}
