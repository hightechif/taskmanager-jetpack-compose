package com.hightechif.taskmanager.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hightechif.taskmanager.domain.TaskCategory
import com.hightechif.taskmanager.ui.theme.TaskManagerTheme

@Composable
fun Chip(
    category: TaskCategory,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = category.color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.displayName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = category.color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    TaskManagerTheme {
        Chip(category = TaskCategory.WORK, modifier = Modifier.padding(8.dp))
    }
}