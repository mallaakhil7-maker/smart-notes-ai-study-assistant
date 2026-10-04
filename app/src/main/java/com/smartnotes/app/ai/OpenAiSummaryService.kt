package com.smartnotes.app.ai

interface AiService {
    suspend fun summarize(text: String): Result<String>
}
