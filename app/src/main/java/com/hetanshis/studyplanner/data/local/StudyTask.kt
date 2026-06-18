package com.hetanshis.studyplanner.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val subject: String,
    val dueDateMillis: Long,
    val priority: Int,
    val completed: Boolean = false
)
