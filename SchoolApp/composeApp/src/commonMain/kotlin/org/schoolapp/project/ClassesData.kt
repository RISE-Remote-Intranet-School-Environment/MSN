package org.schoolapp.project


data class Course(
    val name: String,
    val teacher: String
)

object CourseRepository {
    val courses = listOf(
        Course("Data Sciences", "Prof. Smith"),
        Course("History", "Dr. Taylor"),
        Course("Math", "Dr. Brown")
    )
}
