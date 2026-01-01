package com.learnupp.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learnupp.DialogType
import com.learnupp.util.LearnUppNonPrimaryColors
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue

@Composable
fun AlertDialogWithBlur(
    isVisible: Boolean,
    title: String,
    message: String? = null,
    annotatedMessage: AnnotatedString? = null,
    dialogType: DialogType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    confirmText: String = LearnUppStrings.CONFIRM.getValue(),
    dismissText: String = LearnUppStrings.CANCEL.getValue(),
    buttonCount: Int = 1,
    isConfirmPositive: Boolean = false
) {
    val scrollState = rememberScrollState()

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandIn(), // Fade in and expand
        exit = fadeOut() + shrinkOut() // Fade out and shrink
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)) // Dimming effect
                .padding(30.dp) // Padding of the dialog from the screen edges
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Cap dialog height to 90 % of screen  */
                BoxWithConstraints {
                    val dialogMaxHeight = maxHeight * 0.9f
                    Column(
                        modifier = Modifier
                            .heightIn(max = dialogMaxHeight)
                            .padding(vertical = 15.dp, horizontal = 25.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icon
                        Icon(
                            imageVector = when (dialogType) {
                                DialogType.ERROR -> Icons.Outlined.Cancel
                                DialogType.QUESTION -> Icons.Filled.Help
                                DialogType.SUCCESS -> Icons.Outlined.CheckCircle
                                DialogType.INFO -> Icons.Outlined.Info
                            },
                            tint = when (dialogType) {
                                DialogType.ERROR -> LearnUppNonPrimaryColors.RED
                                DialogType.QUESTION -> LearnUppNonPrimaryColors.WARNING_ORANGE
                                DialogType.SUCCESS -> LearnUppNonPrimaryColors.SUCCESS_GREEN
                                DialogType.INFO -> MaterialTheme.colorScheme.primary
                            },
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        /* — scrollable message — */
                        /* ───── inside your Column, replace the Box that holds the Text ───── */

                        if (!message.isNullOrEmpty() || !annotatedMessage.isNullOrEmpty()) {
                            Spacer(Modifier.height(8.dp))

                            /* one Box acts as both the scroll container and the overlay host */
                            Box(
                                modifier = Modifier
                                    .weight(1f, fill = false)
                                    .fillMaxWidth()
                            ) {
                                /* 1️⃣  The scrollable message itself */
                                Box(
                                    modifier = Modifier
                                        .verticalScroll(scrollState)
                                        .padding(horizontal = 4.dp)
                                        .align(Alignment.TopCenter)
                                ) {
                                    if (!annotatedMessage.isNullOrEmpty()) {
                                        // If annotated message is provided, use it
                                        Text(
                                            text = annotatedMessage,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    } else if (!message.isNullOrEmpty()) {
                                        // Fallback to plain text message
                                        Text(
                                            text = message,
                                            fontSize = 16.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                /* 2️⃣  TOP fade — visible when scrolled down */
                                this@Column.AnimatedVisibility(
                                    visible = scrollState.value > 0,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(18.dp)            // small height just for the hint
                                        .align(Alignment.TopCenter),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier.background(
                                            Brush.verticalGradient(
                                                0.0f to MaterialTheme.colorScheme.background,
                                                1.0f to MaterialTheme.colorScheme.background.copy(alpha = 0f)
                                            )
                                        )
                                    )
                                }

                                /* 3️⃣  BOTTOM fade — visible when there is more below */
                                this@Column.AnimatedVisibility(
                                    visible = scrollState.value < scrollState.maxValue,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(18.dp)
                                        .align(Alignment.BottomCenter),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    Box(
                                        modifier = Modifier.background(
                                            Brush.verticalGradient(
                                                0.0f to MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                                1.0f to MaterialTheme.colorScheme.background
                                            )
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button(s)
                        Column {
                            val filledButtonColors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.background,
                                disabledContentColor = MaterialTheme.colorScheme.primary, // not used
                                disabledContainerColor = MaterialTheme.colorScheme.primary // not used
                            )

                            val outlinedButtonColors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContentColor = MaterialTheme.colorScheme.background, // not used
                                disabledContainerColor = MaterialTheme.colorScheme.background // not used
                            )

                            if (buttonCount > 0) {
                                // Single button or Confirm button in case of two buttons
                                Button(
                                    onClick = onConfirm,
                                    colors = if (buttonCount == 1) filledButtonColors
                                    else if (isConfirmPositive) filledButtonColors
                                    else outlinedButtonColors,
                                    modifier = Modifier
                                        .fillMaxWidth(), // Makes the button take full width
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    // Button text
                                    Text(
                                        text = confirmText,
                                        textAlign = TextAlign.Center,
                                        color = if (buttonCount == 1) MaterialTheme.colorScheme.background
                                        else if (isConfirmPositive)
                                            MaterialTheme.colorScheme.background
                                        else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            if (buttonCount == 2) {
                                Spacer(modifier = Modifier.height(2.dp)) // Adds vertical space between buttons

                                // Dismiss button in case of two buttons
                                Button(
                                    onClick = onDismiss,
                                    colors = if (isConfirmPositive) outlinedButtonColors
                                    else filledButtonColors,
                                    modifier = Modifier
                                        .fillMaxWidth(), // Makes the button take full width
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.primary
                                    ), // border just to make the buttons have the same size
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = dismissText,
                                        textAlign = TextAlign.Center,
                                        color = if (isConfirmPositive)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.background
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
