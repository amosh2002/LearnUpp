package com.learnupp.util

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op for iOS; back navigation is handled via swipe gestures in App.kt
}