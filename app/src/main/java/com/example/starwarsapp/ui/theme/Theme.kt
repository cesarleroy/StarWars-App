package com.example.starwarsapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StarWarsDarkColors = darkColorScheme(
    primary = Color(0xFFFFE81F),       // Amarillo Star Wars
    onPrimary = Color.Black,
    secondary = Color(0xFFB0B0C0),
    background = Color(0xFF0A0A0F),    // Negro espacial
    surface = Color(0xFF1A1A2E),
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFFF6B6B)
)

@Composable
fun StarWarsAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = StarWarsDarkColors,
        content = content
    )
}
