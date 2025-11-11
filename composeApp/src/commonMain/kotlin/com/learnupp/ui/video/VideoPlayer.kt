package com.learnupp.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Expect/actual composable video player.
 * The implementation should autoplay when [playVideoWhenReady] is true.
 */
@Composable
expect fun PlatformVideoPlayer(
    url: String,
    playVideoWhenReady: Boolean,
    modifier: Modifier = Modifier,
    onClicked: (() -> Unit)? = null,
    isMuted: Boolean = false, // Mute/unmute control
)


