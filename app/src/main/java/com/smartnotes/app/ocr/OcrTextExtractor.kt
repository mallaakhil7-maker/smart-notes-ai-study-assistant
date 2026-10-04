package com.smartnotes.app.data

import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun observeNotes(): Flow<List<NoteEntity>>
    fun observeSubjects(): Flow<List<SubjectEntity>>
    suspend fun saveNote(note: NoteEntity): Long
    suspend fun deleteNote(id: Long)
    suspend fun searchNotes(query: String): List<NoteEntity>
    suspend fun saveSubject(subject: SubjectEntity): Long
    suspend fun markNoteViewed(id: Long)
}

class RoomNoteRepository(
    private val database: AppDatabase
) : AppRepository {
    private val subjectDao = database.subjectDao()
    private val noteDao = database.noteDao()

    override fun observeNotes(): Flow<List<NoteEntity>> = noteDao.observeNotes()

    override fun observeSubjects(): Flow<List<SubjectEntity>> = subjectDao.observeSubjects()

    override suspend fun saveNote(note: NoteEntity): Long {
        return if (note.id == 0L) {
            noteDao.insert(note)
        } else {
            noteDao.update(note)
            note.id
        }
    }

    override suspend fun deleteNote(id: Long) {
        noteDao.deleteById(id)
    }

    override suspend fun searchNotes(query: String): List<NoteEntity> {
        return noteDao.searchNotes(query)
    }

    override suspend fun saveSubject(subject: SubjectEntity): Long {
        return subjectDao.insert(subject)
    }

    override suspend fun markNoteViewed(id: Long) {
        noteDao.markViewed(id, System.currentTimeMillis())
    }
}
