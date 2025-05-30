package com.hightechif.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hightechif.taskmanager.data.ThemeMode
import com.hightechif.taskmanager.domain.Task
import com.hightechif.taskmanager.ui.composable.TaskItem
import com.hightechif.taskmanager.ui.composable.ThemeSelector
import com.hightechif.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentTheme by remember { mutableStateOf(ThemeMode.LIGHT) }

            TaskManagerTheme(
                darkTheme = when (currentTheme) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskManagerApp(
                        currentTheme = currentTheme,
                        onThemeChange = { currentTheme = it }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskManagerApp(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    // State for the list of tasks
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskText by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        // Header with theme selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Tasks",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ThemeSelector(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }


        // Add new task section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    placeholder = { Text("Enter a new task...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            tasks = tasks + Task(
                                id = nextId,
                                title = newTaskText.trim()
                            )
                            nextId++
                            newTaskText = ""
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }

        // Task statistics
        val completedCount = tasks.count { it.isCompleted }
        val totalCount = tasks.size

        if (totalCount > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Progress: $completedCount/$totalCount tasks completed",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Tasks list
        if (tasks.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tasks yet! Add one above to get started.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { taskId ->
                            tasks = tasks.map {
                                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted)
                                else it
                            }
                        },
                        onDelete = { taskId ->
                            tasks = tasks.filter { it.id != taskId }
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskManagerPreview() {
    TaskManagerTheme {
        TaskManagerApp(
            currentTheme = ThemeMode.SYSTEM,
            onThemeChange = {}
        )
    }
}