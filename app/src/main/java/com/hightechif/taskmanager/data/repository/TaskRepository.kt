package com.hightechif.taskmanager.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.hightechif.taskmanager.data.ThemeMode
import com.hightechif.taskmanager.data.entities.SerializableTask
import com.hightechif.taskmanager.domain.Task
import com.hightechif.taskmanager.domain.TaskCategory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TaskRepository(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    companion object {
        private const val PREF_NAME = "task_manager_prefs"
        private const val KEY_TASKS = "tasks"
        private const val KEY_NEXT_ID = "next_id"
        private const val KEY_THEME = "theme_mode"
    }

    private fun saveTasks(tasks: List<Task>) {
        val serializableTasks = tasks.map { task ->
            SerializableTask(
                id = task.id,
                title = task.title,
                category = task.category.name,
                isCompleted = task.isCompleted
            )
        }

        val tasksJson = json.encodeToString(serializableTasks)
        sharedPreferences.edit {
            putString(KEY_TASKS, tasksJson)
        }
    }

    fun loadTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString(KEY_TASKS, null)
        return if (tasksJson != null) {
            try {
                val serializableTasks = json.decodeFromString<List<SerializableTask>>(tasksJson)
                serializableTasks.map { serializableTask ->
                    Task(
                        id = serializableTask.id,
                        title = serializableTask.title,
                        category = TaskCategory.valueOf(serializableTask.category),
                        isCompleted = serializableTask.isCompleted
                    )
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun saveNextId(nextId: Int) {
        sharedPreferences.edit {
            putInt(KEY_NEXT_ID, nextId)
        }
    }

    fun loadNextId(): Int {
        return sharedPreferences.getInt(KEY_NEXT_ID, 1)
    }

    fun saveTheme(themeMode: ThemeMode) {
        sharedPreferences.edit {
            putString(KEY_THEME, themeMode.name)
        }
    }

    fun loadTheme(): ThemeMode {
        val themeName = sharedPreferences.getString(KEY_THEME, ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(themeName ?: ThemeMode.SYSTEM.name)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        val currentTasks = loadTasks()
        val updatedTasks = currentTasks.map { task ->
            if (task.id == taskId) {
                task.copy(isCompleted = !task.isCompleted)
            } else {
                task
            }
        }
        saveTasks(updatedTasks)
    }

    fun addTask(task: Task): Int {
        val currentTasks = loadTasks()
        val currentNextId = loadNextId()
        val newTask = task.copy(id = currentNextId)
        val updatedTasks = currentTasks + newTask

        saveTasks(updatedTasks)
        val newNextId = currentNextId + 1
        saveNextId(newNextId)

        return newNextId
    }

    fun deleteTask(taskId: Int) {
        val currentTasks = loadTasks()
        val updatedTasks = currentTasks.filter { it.id != taskId }
        saveTasks(updatedTasks)
    }

    fun clearAllTasks() {
        saveTasks(emptyList())
        saveNextId(1)
    }
}