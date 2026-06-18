package com.hetanshis.studyplanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {

    @Query("SELECT * FROM study_tasks ORDER BY dueDateMillis ASC")
    fun getAllTasks(): Flow<List<StudyTask>>

    @Query("""
        SELECT * FROM study_tasks
        WHERE title LIKE '%' || :query || '%' 
           OR subject LIKE '%' || :query || '%'
        ORDER BY dueDateMillis ASC
    """)
    fun searchTasks(query: String): Flow<List<StudyTask>>

    @Query("SELECT * FROM study_tasks WHERE completed = 0 ORDER BY dueDateMillis ASC")
    fun getIncompleteTasks(): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask)

    @Update
    suspend fun updateTask(task: StudyTask)

    @Delete
    suspend fun deleteTask(task: StudyTask)
}
