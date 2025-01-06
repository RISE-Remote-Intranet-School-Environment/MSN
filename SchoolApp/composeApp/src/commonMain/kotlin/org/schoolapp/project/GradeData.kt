package org.schoolapp.project

// Data classes
data class GradeItem(
    val name: String,
    val ratio: String,
    val score: String,
    val maxScore: String
)



// Example grades
val exampleGrades = listOf(
    Grade("PHY", "Labo 2 : Optic", 20, 14, 20),
    Grade("CHE", "Report Oxydo-reduction", 35, 8, 10),
    Grade("LAW", "Essay Constitution", 60, 10, 30),
    Grade("THE", "Exercices : Bernouilli", 5, 4, 15)
)
