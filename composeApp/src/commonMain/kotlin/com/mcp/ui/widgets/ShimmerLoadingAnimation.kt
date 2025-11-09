package com.mcp.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerLoadingAnimation(modifier: Modifier = Modifier.fillMaxSize()) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "shimmerTranslate"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.5f), // Base light gray
        Color.White.copy(alpha = 0.2f),     // Shiny highlight
        Color.LightGray.copy(alpha = 0.5f)  // Back to base
    )

    Box(modifier = modifier) {
        Canvas(modifier = modifier) {
            val canvasWidth = size.width

            // Calculate the offset for the gradient
            val offset = translateAnim.value * (canvasWidth * 6) - canvasWidth

            // Create a linear gradient that moves horizontally
            val gradientBrush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(offset, 0f),
                end = Offset(offset + canvasWidth, 0f)
            )

            // Draw the rectangle with the moving gradient
            drawRect(
                brush = gradientBrush,
                topLeft = Offset.Zero,
                size = size
            )
        }
    }
}