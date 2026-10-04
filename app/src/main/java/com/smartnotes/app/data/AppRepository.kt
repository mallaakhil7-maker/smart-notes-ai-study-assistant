package com.smartnotes.app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SubjectEntity::class, NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun noteDao(): NoteDao
}
