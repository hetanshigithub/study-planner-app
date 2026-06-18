package com.hetanshis.studyplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hetanshis.studyplanner.data.TaskRepository
import com.hetanshis.studyplanner.data.prefs.UserPreferencesRepository

class StudyPlannerViewModelFactory(
    private val taskRepository: TaskRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyPlannerViewModel::class.java)) {
            return StudyPlannerViewModel(taskRepository, prefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
