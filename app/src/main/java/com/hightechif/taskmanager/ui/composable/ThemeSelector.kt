package com.hightechif.taskmanager.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hightechif.taskmanager.data.ThemeMode
import com.hightechif.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun ThemeSelector(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    var showThemeMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { showThemeMenu = true }
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "Theme Options",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = showThemeMenu,
            onDismissRequest = { showThemeMenu = false }
        ) {
            ThemeMode.entries.forEach { theme ->
                ThemeOptionItem(theme, currentTheme, onThemeChange, onClick = {
                    showThemeMenu = false
                })
            }
        }
    }
}

@Composable
fun ThemeOptionItem(
    selectedTheme: ThemeMode,
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = currentTheme == selectedTheme,
                    onClick = {
                        onThemeChange(selectedTheme)
                        onClick.invoke()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (selectedTheme) {
                        ThemeMode.LIGHT -> "Light Theme"
                        ThemeMode.DARK -> "Dark Theme"
                        ThemeMode.SYSTEM -> "System Theme"
                    }
                )
            }
        },
        onClick = {
            onThemeChange(selectedTheme)
            onClick.invoke()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ThemeSelectorPreview() {
    TaskManagerTheme {
        ThemeSelector(
            currentTheme = ThemeMode.LIGHT,
            onThemeChange = { },
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeOptionItemPreview() {
    TaskManagerTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ThemeOptionItem(
                selectedTheme = ThemeMode.LIGHT,
                currentTheme = ThemeMode.SYSTEM,
                onThemeChange = { },
                onClick = { }
            )
            ThemeOptionItem(
                selectedTheme = ThemeMode.DARK,
                currentTheme = ThemeMode.SYSTEM,
                onThemeChange = { },
                onClick = { }
            )
            ThemeOptionItem(
                selectedTheme = ThemeMode.SYSTEM,
                currentTheme = ThemeMode.SYSTEM,
                onThemeChange = { },
                onClick = { }
            )
        }
    }
}