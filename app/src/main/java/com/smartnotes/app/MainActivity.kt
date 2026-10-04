package com.smartnotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.smartnotes.app.ai.OpenAiSummaryService
import com.smartnotes.app.data.AppDatabase
import com.smartnotes.app.data.RoomNoteRepository
import com.smartnotes.app.ocr.MlKitTextExtractor
import com.smartnotes.app.ui.SmartNotesApp
import com.smartnotes.app.ui.SmartNotesViewModel
import com.smartnotes.app.ui.theme.SmartNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "smart_notes.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = RoomNoteRepository(database)
        val ocrTextExtractor = MlKitTextExtractor(this)
        val aiService = OpenAiSummaryService(BuildConfig.OPENAI_API_KEY)

        val viewModel = SmartNotesViewModel(repository, ocrTextExtractor, aiService)

        setContent {
            SmartNotesTheme {
                SmartNotesApp(viewModel = viewModel)
            }
        }
    }
}
