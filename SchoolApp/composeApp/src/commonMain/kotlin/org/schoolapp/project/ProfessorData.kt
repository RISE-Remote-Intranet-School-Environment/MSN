package org.schoolapp.project


data class Professor(
    val name: String,
    val courseCount: Int,
    val image: Int,
    val email: String,
    val initials: String,
    val course: String
)

object ProfessorRepository {
    val professors = listOf(
        Professor("Mme Dupont", 3, R.drawable.okok, "mme.dupont@mail.com", "DUP", "Data Science"),
        Professor("M. Martin", 5, R.drawable.okok, "m.martin@mail.com", "MAR", "History"),
        Professor("Mme Durand", 4, R.drawable.okok, "mme.durand@mail.com", "DUR", "Science"),
        Professor("M. Lefevre", 2, R.drawable.okok, "m.lefevre@mail.com", "LEF", "Mathematics"),
        Professor("Mme Moreau", 6, R.drawable.okok, "mme.moreau@mail.com", "MOR", "Physics")
    )
}
