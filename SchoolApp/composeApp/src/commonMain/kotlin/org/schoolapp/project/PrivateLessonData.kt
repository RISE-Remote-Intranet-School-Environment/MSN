package org.schoolapp.project

// Data class for Tutor
data class Tutor(
    val name: String,
    val rating: Int,
    val courses: String,
    val description: String
)

object TutorRepository {
    val tutors = listOf(
        Tutor("Samir", 4, "Math", "Samir est un professeur expérimenté en Mathématiques, passionné par l'enseignement des concepts complexes de manière simple."),
        Tutor("Haitam", 5, "Python", "Haitam est un expert en Python avec plus de 10 ans d'expérience en développement et enseignement."),
        Tutor("Yllke", 4, "Java", "Yllke est une passionnée de Java qui aime partager ses connaissances avec ses étudiants."),
        Tutor("Timothé", 3, "English", "Timothé est un enseignant dévoué en Anglais, spécialisé dans l'amélioration des compétences orales."),
        Tutor("Ayoub", 5, "Rust", "Ayoub est un spécialiste de Rust, enthousiaste à l'idée d'enseigner ce langage moderne et performant."),
        Tutor("Ibrahim", 5, "Linux", "Ibrahim est un habitué de l'environnement Linux impatient de partager son savoir.")
    )
}
