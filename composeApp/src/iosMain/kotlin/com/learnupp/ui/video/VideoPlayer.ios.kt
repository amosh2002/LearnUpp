package com.learnupp.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.AVURLAsset
import platform.AVFoundation.cancelPendingSeeks
import platform.AVFoundation.isMuted
import platform.AVFoundation.muted
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.seekToTime
import platform.CoreGraphics.CGRectMake
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSURL
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.darwin.NSObjectProtocol

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformVideoPlayer(
    url: String,
    playVideoWhenReady: Boolean,
    modifier: Modifier,
    onClicked: (() -> Unit)?,
    isMuted: Boolean
) {
    // Build player state once per URL
    val state = remember(url) {
        val nsUrl = NSURL.URLWithString(url) ?: return@remember null
        val item = AVPlayerItem.playerItemWithAsset(AVURLAsset(uRL = nsUrl, options = null))
        val player = AVPlayer.playerWithPlayerItem(item)
        PlayerStateIOS(player = player, item = item)
    }

    // Loop: on end -> seek to 0 -> play again (endless loop like Instagram/TikTok)
    LaunchedEffect(state) {
        state ?: return@LaunchedEffect
        val obs = NSNotificationCenter.defaultCenter.addObserverForName(
            name = AVPlayerItemDidPlayToEndTimeNotification,
            `object` = state.item,
            queue = null
        ) { _ ->
            // Always restart video when it ends (endless loop)
            state.player.seekToTime(CMTimeMake(value = 0, timescale = 1))
            // Play again immediately for endless loop (like Instagram/TikTok)
            // The update block will handle pausing if playVideoWhenReady is false
            state.player.play()
        }
        state.endObserver = obs
    }

    UIKitView(
        modifier = modifier,
        factory = {
            // ✅ Use designated initializer init(frame:)
            val view = UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0)).apply {
                userInteractionEnabled = false
                clipsToBounds = true
                backgroundColor = UIColor.blackColor
            }

            state?.let {
                val layer = AVPlayerLayer.playerLayerWithPlayer(it.player)
                layer.videoGravity = AVLayerVideoGravityResizeAspectFill
                layer.frame = view.layer.bounds
                view.layer.addSublayer(layer)
                it.layer = layer
            }

            view
        },
        update = { view ->
            state?.let {
                // keep layer sized to view bounds
                it.layer?.frame = view.layer.bounds
                // toggle play/pause
                if (playVideoWhenReady) it.player.play() else it.player.pause()
                // toggle mute/unmute
                it.player.muted = isMuted
            }
        }
    )

    // Clean up
    DisposableEffect(state) {
        onDispose {
            state?.let {
                it.player.pause()
                it.item.cancelPendingSeeks()
                it.endObserver?.let { obs ->
                    NSNotificationCenter.defaultCenter.removeObserver(obs)
                }
                it.endObserver = null
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class PlayerStateIOS(
    val player: AVPlayer,
    val item: AVPlayerItem
) {
    var layer: AVPlayerLayer? = null
    var endObserver: NSObjectProtocol? = null // more precise than Any?
}
