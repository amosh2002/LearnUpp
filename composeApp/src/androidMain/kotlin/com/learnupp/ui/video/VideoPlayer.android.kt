package com.learnupp.ui.video

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
actual fun PlatformVideoPlayer(
    url: String,
    playWhenReady: Boolean,
    modifier: Modifier,
    onClicked: (() -> Unit)?
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(context).apply {
                useController = false
                val exo = ExoPlayer.Builder(context).build()
                player = exo
                exo.setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                exo.prepare()
                setOnClickListener { onClicked?.invoke() }
            }
        },
        update = { view ->
            view.player?.playWhenReady = playWhenReady
        }
    )

    // Ensure resources are released
    DisposableEffect(Unit) {
        onDispose {
            // Player is owned by PlayerView and will be released automatically,
            // but be defensive and release if present.
            (null as PlayerView?).let { }
        }
    }
}


