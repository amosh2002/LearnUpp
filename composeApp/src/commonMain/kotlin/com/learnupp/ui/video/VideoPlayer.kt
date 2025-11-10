package com.learnupp.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Expect/actual composable video player.
 * The implementation should autoplay when [playWhenReady] is true.
 */
@Composable
expect fun PlatformVideoPlayer(
    url: String,
    playWhenReady: Boolean,
    modifier: Modifier = Modifier,
    onClicked: (() -> Unit)? = null,
)


