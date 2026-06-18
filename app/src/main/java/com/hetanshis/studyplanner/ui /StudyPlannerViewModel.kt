package com.hetanshis.studyplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hetanshis.studyplanner.data.TaskRepository
import com.hetanshis.studyplanner.data.local.StudyTask
import com.hetanshis.studyplanner.data.prefs.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StudyPlannerViewModel(
    private val taskRepository: TaskRepository,
    private val prefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlannerUiState())
    val uiState: StateFlow<PlannerUiState> = _uiState.asStateFlow()

    private var tasksCollectorJob: Job? = null

    init {
        observeTasks()
        observeUserPrefs()
    }

    private fun observeTasks() {
        tasksCollectorJob?.cancel()
        tasksCollectorJob = viewModelScope.launch {
            taskRepository.getAllTasks().collectLatest { tasks ->
                applyFiltersAndUpdate(tasks)
            }
        }
    }

    private fun observeUserPrefs() {
        viewModelScope.launch {
            prefsRepository.usernameFlow.collectLatest { name ->
                _uiState.value = _uiState.value.copy(currentUsername = name)
            }
        }
        viewModelScope.launch {
            prefsRepository.darkThemeFlow.collectLatest { dark ->
                _uiState.value = _uiState.value.copy(useDarkTheme = dark)
            }
        }
    }

    private fun applyFiltersAndUpdate(allTasks: List<StudyTask>) {
        val state = _uiState.value
        val filtered = allTasks.filter { task ->
            val matchesText =
                state.searchText.isBlank() ||
                        task.title.contains(state.searchText, ignoreCase = true) ||
                        task.subject.contains(state.searchText, ignoreCase = true)
            val matchesCompletion =
                !state.showOnlyIncomplete || !task.completed
            matchesText && matchesCompletion
        }

        _uiState.value = state.copy(tasks = filtered)
    }

    fun onSearchTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
        refreshTasks()
    }

    fun onToggleIncompleteOnly() {
        _uiState.value = _uiState.value.copy(
            showOnlyIncomplete = !_uiState.value.showOnlyIncomplete
        )
        refreshTasks()
    }

    private fun refreshTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collectLatest { tasks ->
                applyFiltersAndUpdate(tasks)
            }
        }
    }

    fun onAddTaskClicked() {
        _uiState.value = _uiState.value.copy(
            isAddingOrEditing = true,
            editingTask = null
        )
    }

    fun onEditTaskClicked(task: StudyTask) {
        _uiState.value = _uiState.value.copy(
            isAddingOrEditing = true,
            editingTask = task
        )
    }

    fun onDialogDismiss() {
        _uiState.value = _uiState.value.copy(
            isAddingOrEditing = false,
            editingTask = null
        )
    }

    fun onSaveTask(
        title: String,
        subject: String,
        dueDateMillis: Long,
        priority: Int,
        completed: Boolean
    ) {
        viewModelScope.launch {
            val existing = _uiState.value.editingTask
            if (existing == null) {
                taskRepository.addTask(
                    StudyTask(
                        title = title,
                        subject = subject,
                        dueDateMillis = dueDateMillis,
                        priority = priority,
                        completed = completed
                    )
                )
            } else {
                taskRepository.updateTask(
                    existing.copy(
                        title = title,
                        subject = subject,
                        dueDateMillis = dueDateMillis,
                        priority = priority,
                        completed = completed
                    )
                )
            }
            onDialogDismiss()
        }
    }

    fun onDeleteTask(task: StudyTask) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun onTaskCompletionChanged(task: StudyTask, completed: Boolean) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(completed = completed))
        }
    }

    fun onUsernameChanged(newName: String) {
        viewModelScope.launch {
            prefsRepository.setUsername(newName)
        }
    }

    fun onThemeChanged(useDark: Boolean) {
        viewModelScope.launch {
            prefsRepository.setDarkTheme(useDark)
        }
    }
}
