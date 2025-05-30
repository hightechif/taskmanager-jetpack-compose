package com.hightechif.taskmanager.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class SerializableTask(
    val id: Int,
    val title: String,
    val category: String,
    val isCompleted: Boolean
)