package com.smartnotes.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY lastViewedAt DESC")
    fun observeNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    suspend fun searchNotes(query: String): List<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Update
    suspend fun update(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE notes SET lastViewedAt = :timestamp WHERE id = :id")
    suspend fun markViewed(id: Long, timestamp: Long)
}

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subjects ORDER BY name ASC")
    fun observeSubjects(): Flow<List<SubjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: SubjectEntity): Long

    @Query("DELETE FROM subjects WHERE id = :id")
    suspend fun deleteById(id: Long)
}
