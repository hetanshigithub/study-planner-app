package com.hetanshis.studyplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.hetanshis.studyplanner.data.TaskRepository
import com.hetanshis.studyplanner.data.local.StudyPlannerDatabase
import com.hetanshis.studyplanner.data.prefs.UserPreferencesRepository
import com.hetanshis.studyplanner.ui.StudyPlannerApp
import com.hetanshis.studyplanner.ui.StudyPlannerViewModel
import com.hetanshis.studyplanner.ui.StudyPlannerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = StudyPlannerDatabase.getInstance(applicationContext)
        val taskRepo = TaskRepository(database.taskDao())
        val prefsRepo = UserPreferencesRepository(applicationContext)

        val factory = StudyPlannerViewModelFactory(taskRepo, prefsRepo)
        val viewModel = ViewModelProvider(this, factory)[StudyPlannerViewModel::class.java]

        setContent {
            StudyPlannerApp(viewModel = viewModel)
        }
    }
}
