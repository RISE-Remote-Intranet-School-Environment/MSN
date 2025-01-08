package org.schoolapp.project

data class ToDoItem(val title: String, val isCompleted: Boolean)

fun generateToDoItems(): List<ToDoItem> {
    return listOf(
        ToDoItem("Complete homework", false),
        ToDoItem("Finish MVVM", true),
        ToDoItem("Prepare for presentation", true)

    )
}

