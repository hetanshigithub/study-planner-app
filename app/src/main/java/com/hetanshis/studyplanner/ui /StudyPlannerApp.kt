package com.hetanshis.studyplanner.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hetanshis.studyplanner.data.local.StudyTask
import com.hetanshis.studyplanner.ui.theme.StudyPlannerTheme

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun StudyPlannerApp(
    viewModel: StudyPlannerViewModel
) {
    val state by viewModel.uiState.collectAsState()

    StudyPlannerTheme(darkTheme = state.useDarkTheme) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Study Planner") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { viewModel.onAddTaskClicked() }) {
                    Text("+")
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp)
            ) {
                MainContent(
                    state = state,
                    onSearchChange = viewModel::onSearchTextChanged,
                    onToggleIncomplete = viewModel::onToggleIncompleteOnly,
                    onUsernameChange = viewModel::onUsernameChanged,
                    onThemeChange = viewModel::onThemeChanged,
                    onTaskClickEdit = viewModel::onEditTaskClicked,
                    onTaskDelete = viewModel::onDeleteTask,
                    onTaskCompletedChange = viewModel::onTaskCompletionChanged
                )

                if (state.isAddingOrEditing) {
                    AddEditTaskDialog(
                        existingTask = state.editingTask,
                        onDismiss = viewModel::onDialogDismiss,
                        onSave = { title, subject, dueMillis, pri, done ->
                            viewModel.onSaveTask(
                                title = title,
                                subject = subject,
                                dueDateMillis = dueMillis,
                                priority = pri,
                                completed = done
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    state: PlannerUiState,
    onSearchChange: (String) -> Unit,
    onToggleIncomplete: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onThemeChange: (Boolean) -> Unit,
    onTaskClickEdit: (StudyTask) -> Unit,
    onTaskDelete: (StudyTask) -> Unit,
    onTaskCompletedChange: (StudyTask, Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Greeting + username input
        Text(
            text = "Welcome, ${state.currentUsername}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.currentUsername,
            onValueChange = onUsernameChange,
            label = { Text("Change username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Theme switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dark theme")
            Spacer(Modifier.width(8.dp))
            Switch(
                checked = state.useDarkTheme,
                onCheckedChange = onThemeChange
            )
        }

        Spacer(Modifier.height(12.dp))

        // Search + filter
        OutlinedTextField(
            value = state.searchText,
            onValueChange = onSearchChange,
            label = { Text("Search by title or subject") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        ) {
            Checkbox(
                checked = state.showOnlyIncomplete,
                onCheckedChange = { onToggleIncomplete() }
            )
            Text("Show only incomplete tasks")
        }

        // Task list
        val listState = rememberLazyListState()

        AnimatedVisibility(
            visible = state.tasks.isEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks yet. Tap + to add one.")
            }
        }

        AnimatedVisibility(
            visible = state.tasks.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = state.tasks,
                    key = { it.id }
                ) { task ->
                    TaskItem(
                        task = task,
                        onEdit = { onTaskClickEdit(task) },
                        onDelete = { onTaskDelete(task) },
                        onCheckedChange = { checked ->
                            onTaskCompletedChange(task, checked)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: StudyTask,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text("Subject: ${task.subject}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Priority: ${priorityLabel(task.priority)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Checkbox(
                    checked = task.completed,
                    onCheckedChange = onCheckedChange
                )
                Text("Done", style = MaterialTheme.typography.bodySmall)

                Spacer(Modifier.height(4.dp))

                Row {
                    TextButton(onClick = onEdit) {
                        Text("Edit")
                    }
                    TextButton(onClick = onDelete) {
                        Text("Del")
                    }
                }
            }
        }
    }
}

private fun priorityLabel(priority: Int): String =
    when (priority) {
        1 -> "Low"
        2 -> "Medium"
        3 -> "High"
        else -> "Unknown"
    }
