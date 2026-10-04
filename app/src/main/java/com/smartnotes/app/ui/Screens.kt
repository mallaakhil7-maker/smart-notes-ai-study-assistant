package com.smartnotes.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartnotes.app.ai.AiService
import com.smartnotes.app.data.NoteEntity
import com.smartnotes.app.data.SubjectEntity
import com.smartnotes.app.data.AppRepository
import com.smartnotes.app.ocr.OcrTextExtractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class SmartNotesUiState(
    val notes: List<NoteEntity> = emptyList(),
    val subjects: List<SubjectEntity> = emptyList(),
    val searchQuery: String = "",
    val summaryText: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)

class SmartNotesViewModel(
    private val repository: AppRepository,
    private val ocrTextExtractor: OcrTextExtractor,
    private val aiService: AiService
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _summaryText = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _infoMessage = MutableStateFlow<String?>(null)

    val notes = repository.observeNotes().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val subjects = repository.observeSubjects().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<SmartNotesUiState> = combineStates(
        notes,
        subjects,
        _searchQuery,
        _summaryText,
        _isLoading,
        _errorMessage,
        _infoMessage
    ).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SmartNotesUiState()
    )

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun clearMessages() {
        _errorMessage.value = null
        _infoMessage.value = null
    }

    fun saveSubject(name: String) {
        val cleaned = name.trim()
        if (cleaned.isBlank()) {
            _errorMessage.value = "Subject name cannot be empty."
            return
        }
        viewModelScope.launch {
            repository.saveSubject(SubjectEntity(name = cleaned))
            _infoMessage.value = "Subject saved."
        }
    }

    fun saveNote(title: String, content: String, subjectId: Long? = null, tags: String = "") {
        val noteTitle = title.ifBlank { "Untitled Note" }
        val noteContent = content.trim()
        if (noteContent.isEmpty()) {
            _errorMessage.value = "Note content cannot be empty."
            return
        }

        val subject = subjects.value.firstOrNull { it.id == subjectId }

        viewModelScope.launch {
            repository.saveNote(
                NoteEntity(
                    title = noteTitle,
                    content = noteContent,
                    subjectId = subjectId,
                    subjectName = subject?.name,
                    tags = tags,
                    updatedAt = System.currentTimeMillis(),
                    lastViewedAt = System.currentTimeMillis()
                )
            )
            _infoMessage.value = "Note saved successfully."
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            _infoMessage.value = "Note deleted permanently."
        }
    }

    fun searchNotes(): List<NoteEntity> {
        val query = _searchQuery.value.trim()
        if (query.isBlank()) return notes.value
        return notes.value.filter { note ->
            val haystack = listOf(
                note.title,
                note.content,
                note.subjectName.orEmpty(),
                note.tags
            ).joinToString(" ").lowercase(Locale.getDefault())
            haystack.contains(query.lowercase(Locale.getDefault()))
        }
    }

    fun markNoteViewed(noteId: Long) {
        viewModelScope.launch {
            repository.markNoteViewed(noteId)
        }
    }

    suspend fun extractTextFromImage(uri: android.net.Uri): Result<String> {
        return ocrTextExtractor.extractTextFromUri(uri)
    }

    fun summarizeNote(note: NoteEntity) {
        _isLoading.value = true
        _errorMessage.value = null
        _summaryText.value = null

        viewModelScope.launch {
            val result = aiService.summarize(note.content)
            _isLoading.value = false
            result.onSuccess { summary ->
                _summaryText.value = summary
                _infoMessage.value = "AI summary generated."
            }.onFailure { error ->
                _errorMessage.value = error.message ?: "Unable to summarize this note right now."
            }
        }
    }

    fun uiStateForQuery(searchQuery: String): SmartNotesUiState {
        val activeNotes = if (searchQuery.isBlank()) {
            notes.value
        } else {
            notes.value.filter { note ->
                val haystack = listOf(
                    note.title,
                    note.content,
                    note.subjectName.orEmpty(),
                    note.tags
                ).joinToString(" ").lowercase(Locale.getDefault())
                haystack.contains(searchQuery.lowercase(Locale.getDefault()))
            }
        }

        return SmartNotesUiState(
            notes = activeNotes,
            subjects = subjects.value,
            searchQuery = searchQuery,
            summaryText = _summaryText.value,
            isLoading = _isLoading.value,
            errorMessage = _errorMessage.value,
            infoMessage = _infoMessage.value
        )
    }

    fun getLatestSummary(): String? = _summaryText.value

    companion object {
        private fun combineStates(
            notes: StateFlow<List<NoteEntity>>,
            subjects: StateFlow<List<SubjectEntity>>,
            searchQuery: StateFlow<String>,
            summaryText: StateFlow<String?>,
            isLoading: StateFlow<Boolean>,
            errorMessage: StateFlow<String?>,
            infoMessage: StateFlow<String?>
        ) = kotlinx.coroutines.flow.combine(
            notes, subjects, searchQuery, summaryText, isLoading, errorMessage, infoMessage
        ) { noteList, subjectList, query, summary, loading, error, info ->
            SmartNotesUiState(
                notes = if (query.isBlank()) noteList else noteList.filter { note ->
                    val haystack = listOf(
                        note.title,
                        note.content,
                        note.subjectName.orEmpty(),
                        note.tags
                    ).joinToString(" ").lowercase(Locale.getDefault())
                    haystack.contains(query.lowercase(Locale.getDefault()))
                },
                subjects = subjectList,
                searchQuery = query,
                summaryText = summary,
                isLoading = loading,
                errorMessage = error,
                infoMessage = info
            )
        }
    }
}
