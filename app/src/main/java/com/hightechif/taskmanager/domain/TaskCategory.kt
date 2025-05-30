package com.hightechif.taskmanager.domain

import androidx.compose.ui.graphics.Color

enum class TaskCategory(
    val displayName: String,
    val color: Color
) {
    WORK("Work", Color(0xFF2196F3)),           // Blue
    PERSONAL("Personal", Color(0xFF4CAF50)),   // Green
    SHOPPING("Shopping", Color(0xFFFF9800)),   // Orange
    HEALTH("Health", Color(0xFFE91E63)),       // Pink
    STUDY("Study", Color(0xFF9C27B0)),         // Purple
    OTHER("Other", Color(0xFF607D8B))          // Blue Grey
}