package com.hetanshis.studyplanner.data

import com.hetanshis.studyplanner.data.local.StudyTask
import com.hetanshis.studyplanner.data.local.StudyTaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val dao: StudyTaskDao
) {

    fun getAllTasks(): Flow<List<StudyTask>> = dao.getAllTasks()

    fun searchTasks(query: String): Flow<List<StudyTask>> =
        if (query.isBlank()) dao.getAllTasks() else dao.searchTasks(query)

    fun getIncompleteTasks(): Flow<List<StudyTask>> = dao.getIncompleteTasks()

    suspend fun addTask(task: StudyTask) = dao.insertTask(task)

    suspend fun updateTask(task: StudyTask) = dao.updateTask(task)

    suspend fun deleteTask(task: StudyTask) = dao.deleteTask(task)
}
