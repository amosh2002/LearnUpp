package com.learnupp.ui.base

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryRed = Color(0xFFE50914)
private val PrimaryBlack = Color(0xFF0F0F0F)
private val PrimaryWhite = Color(0xFFFFFFFF)
private val TextPrimaryGray = Color(0xFFADAEBC)
private val StrokeGray = Color(0xFF333333)
private val SurfaceDark = Color(0xFF1A1A1A)
private val BackgroundBlack = PrimaryBlack

private val DarkColors: ColorScheme = darkColorScheme(
    // Brand and accents
    primary = PrimaryRed,
    onPrimary = PrimaryWhite,

    // Secondary accents (used for supporting content/icons)
    secondary = TextPrimaryGray,
    onSecondary = PrimaryBlack,

    // App background and content on it
    background = BackgroundBlack,
    onBackground = PrimaryWhite,

    // Surfaces (cards, sheets, bars) and content on them
    surface = SurfaceDark,
    onSurface = PrimaryWhite,

    // Borders, dividers
    outline = StrokeGray,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content = content
    )
}


