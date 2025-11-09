package com.learnupp.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val SecondaryButtonHeight = 40.dp

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    height: Dp = SecondaryButtonHeight,
    enabled: Boolean = true,
    buttonColors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        disabledContentColor = Color.White.copy(alpha = 0.5f)
    ),
    border: BorderStroke = ButtonDefaults.outlinedButtonBorder.copy(
        brush = SolidColor(MaterialTheme.colorScheme.onPrimary)
    ),
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = buttonColors,
        enabled = enabled,
        border = border,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .height(height)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}