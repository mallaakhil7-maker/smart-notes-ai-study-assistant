package com.smartnotes.app.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MlKitTextExtractor(
    private val context: Context
) : OcrTextExtractor {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun extractTextFromUri(uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val image = InputImage.fromFilePath(context, uri)
            val result = recognizer.process(image).await()
            val extracted = result.textBlocks.joinToString(separator = "\n") { block ->
                block.lines.joinToString(separator = "\n") { line -> line.text }
            }.trim()
            if (extracted.isEmpty()) {
                Result.failure(IllegalStateException("No text could be detected in the selected image."))
            } else {
                Result.success(extracted)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
