package com.hightechif.taskmanager.domain

data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)