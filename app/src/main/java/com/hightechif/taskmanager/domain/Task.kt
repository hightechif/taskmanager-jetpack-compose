package com.hightechif.taskmanager.domain

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val title: String,
    val category: TaskCategory = TaskCategory.PERSONAL,
    val isCompleted: Boolean = false
) {

    companion object {
        fun getDummy() = listOf(
            Task(1, "Finish quarterly report", TaskCategory.WORK, false),
            Task(2, "Team meeting at 2 PM", TaskCategory.WORK, true),
            Task(3, "Buy groceries for dinner", TaskCategory.SHOPPING, false),
            Task(4, "Call mom for birthday", TaskCategory.PERSONAL, false),
            Task(5, "Read Kotlin documentation", TaskCategory.STUDY, true),
            Task(6, "Go to gym", TaskCategory.HEALTH, false),
            Task(7, "Buy new laptop", TaskCategory.SHOPPING, false),
            Task(8, "Complete Android course", TaskCategory.STUDY, false),
            Task(9, "Schedule dentist appointment", TaskCategory.HEALTH, true),
            Task(10, "Plan weekend trip", TaskCategory.PERSONAL, false),
            Task(11, "Review code for new feature", TaskCategory.WORK, true),
            Task(12, "Organize desk space", TaskCategory.OTHER, false)
        )
    }

}