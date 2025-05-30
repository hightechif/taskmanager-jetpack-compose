package com.hightechif.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.hightechif.taskmanager.data.repository.TaskRepository
import com.hightechif.taskmanager.domain.Task
import com.hightechif.taskmanager.domain.TaskCategory
import com.hightechif.taskmanager.ui.composable.CategorySelector
import com.hightechif.taskmanager.ui.composable.TaskItem
import com.hightechif.taskmanager.ui.composable.ThemeSelector
import com.hightechif.taskmanager.ui.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = TaskRepository(this)
            var currentTheme by remember { mutableStateOf(repository.loadTheme()) }

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
                        repository = repository,
                        currentTheme = currentTheme,
                        onThemeChange = { selectedTheme ->
                            currentTheme = selectedTheme
                            repository.saveTheme(selectedTheme)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskManagerApp(
    repository: TaskRepository? = null,
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    initialTasks: List<Task> = emptyList(),
    initialNextId: Int = 1
) {
    // State for the list of tasks
    var tasks by remember { mutableStateOf(initialTasks) }
    var newTaskText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.PERSONAL) }
    var filterCategory by remember { mutableStateOf<TaskCategory?>(null) }
    var nextId by remember { mutableIntStateOf(initialNextId) }
    var isAddTaskSectionExpanded by remember { mutableStateOf(true) }

    // Load data from repository on first composition
    LaunchedEffect(repository) {
        repository?.let {
            tasks = it.loadTasks()
            nextId = it.loadNextId()
        }
    }

    // Filter tasks based on selected category
    val filteredTasks = if (filterCategory != null) {
        tasks.filter { it.category == filterCategory }
    } else {
        tasks
    }

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
            )

            ThemeSelector(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
            )
        }

        // Header row with toggle button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add New Task",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            IconButton(
                onClick = { isAddTaskSectionExpanded = !isAddTaskSectionExpanded },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (isAddTaskSectionExpanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isAddTaskSectionExpanded)
                        "Collapse add task section"
                    else
                        "Expand add task section",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Collapsible Add new task section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Collapsible content
                AnimatedVisibility(
                    visible = isAddTaskSectionExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(top = 8.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        // Task input row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                        val newTask = Task(
                                            id = nextId, // This will be overridden by repository
                                            title = newTaskText.trim(),
                                            category = selectedCategory
                                        )

                                        repository?.let {
                                            nextId = it.addTask(newTask)
                                            tasks = it.loadTasks()
                                        } ?: run {
                                            // Fallback for preview/testing
                                            tasks = tasks + newTask.copy(id = nextId)
                                            nextId++
                                        }
                                        newTaskText = ""
                                    }
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Task")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category selector
                        CategorySelector(
                            selectedCategory = selectedCategory,
                            onCategorySelected = { selectedCategory = it }
                        )
                    }
                }
            }
        }

        // Category filters
        if (tasks.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // "All" filter chip
                item {
                    FilterChip(
                        onClick = { filterCategory = null },
                        label = { Text("All") },
                        selected = filterCategory == null
                    )
                }

                // Category filter chips
                items(TaskCategory.entries) { category ->
                    val categoryCount = tasks.count { it.category == category }
                    if (categoryCount > 0) {
                        FilterChip(
                            onClick = {
                                filterCategory =
                                    if (filterCategory == category) null else category
                            },
                            label = { Text("${category.displayName} ($categoryCount)") },
                            selected = filterCategory == category
                        )
                    }
                }
            }
        }

        // Task statistics
        val completedCount = filteredTasks.count { it.isCompleted }
        val totalCount = filteredTasks.size

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
                    text = if (filterCategory != null) {
                        "Progress (${filterCategory!!.displayName}): $completedCount/$totalCount tasks completed"
                    } else {
                        "Progress: $completedCount/$totalCount tasks completed"
                    },
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Tasks list
        if (filteredTasks.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (tasks.isEmpty()) {
                        "No tasks yet! Add one above to get started."
                    } else {
                        "No tasks in this category."
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { taskId ->
                            repository?.let {
                                it.toggleTaskCompletion(taskId)
                                tasks = it.loadTasks()
                            } ?: run {
                                // Fallback for preview/testing
                                tasks = tasks.map {
                                    if (it.id == taskId) it.copy(isCompleted = !it.isCompleted)
                                    else it
                                }
                            }
                        },
                        onDelete = { taskId ->
                            repository?.let {
                                it.deleteTask(taskId)
                                tasks = it.loadTasks()
                            } ?: run {
                                // Fallback for preview/testing
                                tasks = tasks.filter { it.id != taskId }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
            onThemeChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerWithTwoDummyDataPreview() {
    TaskManagerTheme {
        TaskManagerApp(
            currentTheme = ThemeMode.SYSTEM,
            onThemeChange = {},
            initialTasks = Task.getDummy().take(2),
            initialNextId = 10
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskManagerWithDummyDataPreview() {
    TaskManagerTheme {
        TaskManagerApp(
            currentTheme = ThemeMode.SYSTEM,
            onThemeChange = {},
            initialTasks = Task.getDummy(),
            initialNextId = 12
        )
    }
}