package com.smartnotes.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: String = "#4F46E5"
)

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val subjectId: Long? = null,
    val subjectName: String? = null,
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = createdAt,
    val lastViewedAt: Long = createdAt
)
