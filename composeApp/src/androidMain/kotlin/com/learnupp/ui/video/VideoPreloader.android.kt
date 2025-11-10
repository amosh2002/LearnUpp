package com.learnupp.ui.video

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.learnupp.util.AppContextHolder

private object VideoPreloadManager {
    @Volatile private var player: ExoPlayer? = null
    private fun ensurePlayer(ctx: Context): ExoPlayer {
        return player ?: ExoPlayer.Builder(ctx).build().also { player = it }
    }

    fun prefetch(ctx: Context, url: String) {
        val exo = ensurePlayer(ctx)
        if (exo.currentMediaItem?.localConfiguration?.uri.toString() == url) return
        exo.setMediaItem(MediaItem.fromUri(url))
        exo.playWhenReady = false
        exo.prepare()
    }
}

actual fun prefetchVideo(url: String) {
    val ctx = AppContextHolder.appContext ?: return
    VideoPreloadManager.prefetch(ctx, url)
}


