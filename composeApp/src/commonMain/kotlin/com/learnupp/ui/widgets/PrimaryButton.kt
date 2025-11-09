package com.learnupp.ui.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    buttonColors: ButtonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = Color.White,
        disabledContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        disabledContentColor = Color.White.copy(alpha = 0.5f)
    ),
    onClick: () -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    height: Dp? = null,
) {
    val finalModifier = if (height == null) {
        modifier.wrapContentHeight()
    } else {
        modifier.height(height)
    }

    Button(
        onClick = onClick,
        colors = buttonColors,
        enabled = enabled,
        shape = shape,
        modifier = finalModifier
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(vertical = 2.dp)
        )
    }
}