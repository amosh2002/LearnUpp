package com.learnupp.ui.widgets

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.isSpecified

@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier,
    color: Color = style.color,
    textAlign: TextAlign = TextAlign.Center
) {
    // Track the previous text to detect changes
    var previousText by remember { mutableStateOf(text) }

    // Store the resized style and default font size
    var resizedTextStyle by remember { mutableStateOf(style) }
    val defaultFontSize =
        style.fontSize.takeIf { it.isSpecified } ?: MaterialTheme.typography.bodyLarge.fontSize

    // Reset the font size if the text has changed
    if (previousText != text) {
        resizedTextStyle = style.copy(fontSize = defaultFontSize)
        previousText = text
    }

    // Track whether the content should be drawn
    var shouldDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        textAlign = textAlign,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.98
                )
            } else {
                shouldDraw = true
            }
        }
    )
}