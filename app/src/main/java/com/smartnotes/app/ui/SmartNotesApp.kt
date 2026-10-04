package com.smartnotes.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SmartNotesApp(viewModel: SmartNotesViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToScan = { navController.navigate("scan") },
                onNavigateToSubjects = { navController.navigate("subjects") },
                onNavigateToStudy = { navController.navigate("study") },
                onOpenNote = { noteId -> navController.navigate("noteDetail/$noteId") }
            )
        }
        composable("scan") {
            ScanNotesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
        composable("subjects") {
            SubjectsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("study") {
            StudyScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("noteDetail/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toLongOrNull()
            if (noteId != null) {
                NoteDetailScreen(
                    noteId = noteId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
