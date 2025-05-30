package com.hightechif.taskmanager.domain

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class TaskCategory(
    val displayName: String,
    private val colorValue: Long
) {
    WORK("Work", 0xFF2196F3),           // Blue
    PERSONAL("Personal", 0xFF4CAF50),   // Green
    SHOPPING("Shopping", 0xFFFF9800),   // Orange
    HEALTH("Health", 0xFFE91E63),       // Pink
    STUDY("Study", 0xFF9C27B0),         // Purple
    OTHER("Other", 0xFF607D8B);         // Blue Grey

    val color: Color
        get() = Color(colorValue)

}