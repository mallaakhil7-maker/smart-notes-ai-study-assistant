package com.smartnotes.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF2563EB),
    secondary = androidx.compose.ui.graphics.Color(0xFF7C3AED),
    tertiary = androidx.compose.ui.graphics.Color(0xFF0EA5E9),
    background = androidx.compose.ui.graphics.Color(0xFFF8FAFC),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFFFF),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color(0xFF0F172A),
    onSurface = androidx.compose.ui.graphics.Color(0xFF0F172A)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF60A5FA),
    secondary = androidx.compose.ui.graphics.Color(0xFFA78BFA),
    tertiary = androidx.compose.ui.graphics.Color(0xFF38BDF8),
    background = androidx.compose.ui.graphics.Color(0xFF020817),
    surface = androidx.compose.ui.graphics.Color(0xFF111827),
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE2E8F0),
    onSurface = androidx.compose.ui.graphics.Color(0xFFE2E8F0)
)

@Composable
fun SmartNotesTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
