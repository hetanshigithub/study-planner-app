package com.hetanshis.studyplanner.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hetanshis.studyplanner.data.local.StudyTask
import java.util.concurrent.TimeUnit

@Composable
fun AddEditTaskDialog(
    existingTask: StudyTask?,
    onDismiss: () -> Unit,
    onSave: (String, String, Long, Int, Boolean) -> Unit
) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var subject by remember { mutableStateOf(existingTask?.subject ?: "") }
    var daysFromNow by remember { mutableStateOf("0") }
    var priority by remember { mutableStateOf(existingTask?.priority ?: 2) }
    var completed by remember { mutableStateOf(existingTask?.completed ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (existingTask == null) "Add Task" else "Edit Task")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = daysFromNow,
                    onValueChange = { daysFromNow = it },
                    label = { Text("Due in (days from now)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                PriorityDropdown(
                    selected = priority,
                    onSelectedChange = { priority = it }
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = completed,
                        onCheckedChange = { completed = it }
                    )
                    Text("Completed")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val days = daysFromNow.toLongOrNull() ?: 0L
                    val dueMillis = System.currentTimeMillis() +
                            TimeUnit.DAYS.toMillis(days)
                    onSave(title, subject, dueMillis, priority, completed)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun PriorityDropdown(
    selected: Int,
    onSelectedChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val label = when (selected) {
        1 -> "Low"
        2 -> "Medium"
        3 -> "High"
        else -> "Medium"
    }

    Column {
        Text("Priority")
        Spacer(Modifier.height(4.dp))
        OutlinedButton(onClick = { expanded = true }) {
            Text(label)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Low") },
                onClick = {
                    onSelectedChange(1)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Medium") },
                onClick = {
                    onSelectedChange(2)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("High") },
                onClick = {
                    onSelectedChange(3)
                    expanded = false
                }
            )
        }
    }
}
