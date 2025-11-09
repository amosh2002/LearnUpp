package com.mcp.ui.base

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryBlue = Color(0xFF1565C0)
private val SecondaryGreen = Color(0xFF2E7D32)
private val BackgroundWhite = Color(0xFFFFFFFF)
private val OnPrimary = Color(0xFFFFFFFF)
private val OnBackground = Color(0xFF0F172A)

private val LightColors: ColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = OnPrimary,
    secondary = SecondaryGreen,
    onSecondary = OnPrimary,
    background = BackgroundWhite,
    onBackground = OnBackground,
    surface = BackgroundWhite,
    onSurface = OnBackground,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}


