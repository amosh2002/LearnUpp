package com.learnupp.ui.video

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.core.net.toUri

@OptIn(UnstableApi::class)
@Composable
actual fun PlatformVideoPlayer(
    url: String,
    playVideoWhenReady: Boolean, // <- treat as input; don't mutate
    modifier: Modifier,
    onClicked: (() -> Unit)?
) {
    val context = LocalContext.current

    // Create a single ExoPlayer for this composable instance and keep it across recompositions
    val exo = remember {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE // loop video
            playWhenReady =
                false               // default; we'll drive this via LaunchedEffect below
        }
    }

    // Swap the media item whenever the URL changes
    LaunchedEffect(url) {
        exo.setMediaItem(MediaItem.fromUri(url.toUri()))
        exo.prepare()
    }

    // Drive play/pause from the parent state
    LaunchedEffect(playVideoWhenReady) {
        exo.playWhenReady = playVideoWhenReady
    }

    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(context).apply {
                useController = false
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                player = exo
                // Match iOS AspectFill
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                keepScreenOn = true
                setOnClickListener { onClicked?.invoke() }
            }
        },
        update = {
            // No-op: all updates are driven by the effects above
        }
    )

    // Release player when this composable leaves the composition
    DisposableEffect(Unit) {
        onDispose { exo.release() }
    }
}
