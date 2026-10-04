package com.smartnotes.app.ocr

import android.net.Uri

interface OcrTextExtractor {
    suspend fun extractTextFromUri(uri: Uri): Result<String>
}
