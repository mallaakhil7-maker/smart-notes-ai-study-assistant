package com.smartnotes.app.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class OpenAiSummaryService(
    private val apiKey: String,
    private val model: String = "gpt-4o-mini"
) : AiService {

    private val client = OkHttpClient()

    override suspend fun summarize(text: String): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("OpenAI API key is not configured. Add OPENAI_API_KEY to local.properties."))
        }

        try {
            val prompt = "Summarize the following student notes in a clear, useful way. Include the most important concepts, and keep the result concise and study-friendly.\n\n$text"
            val json = JSONObject().apply {
                put("model", model)
                put(
                    "messages",
                    JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("content", prompt)
                        })
                    }
                )
                put("temperature", 0.3)
            }

            val request = Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .post(json.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                throw IOException("AI request failed: $body")
            }

            val payload = JSONObject(body)
            val choices = payload.getJSONArray("choices")
            val content = choices.getJSONObject(0).getJSONObject("message").getString("content")
            Result.success(content.trim())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
