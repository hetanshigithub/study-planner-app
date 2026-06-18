package com.hetanshis.studyplanner.ui

import com.hetanshis.studyplanner.data.local.StudyTask

data class PlannerUiState(
    val tasks: List<StudyTask> = emptyList(),
    val searchText: String = "",
    val showOnlyIncomplete: Boolean = false,
    val isAddingOrEditing: Boolean = false,
    val editingTask: StudyTask? = null,
    val currentUsername: String = "Student",
    val useDarkTheme: Boolean = false
)
