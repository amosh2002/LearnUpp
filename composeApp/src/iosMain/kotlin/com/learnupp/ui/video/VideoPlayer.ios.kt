package com.learnupp.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformVideoPlayer(
    url: String,
    playWhenReady: Boolean,
    modifier: Modifier,
    onClicked: (() -> Unit)?
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val view = UIView().apply {
                userInteractionEnabled = false // let Compose handle gestures (pager swipes)
                clipsToBounds = true
            }
            val player = NSURL.URLWithString(url)?.let { AVPlayer(uRL = it) }
            val layer = AVPlayerLayer.playerLayerWithPlayer(player)
            // Match the view bounds initially; will keep updated below
            layer.frame = view.layer.bounds
            layer.videoGravity = AVLayerVideoGravityResizeAspectFill
            view.layer.addSublayer(layer)
            view
        },
        update = { view ->
            val layer = view.layer.sublayers?.first() as? AVPlayerLayer
            // Ensure the layer matches current view bounds during layout changes
            layer?.frame = view.layer.bounds
            if (playWhenReady) {
                layer?.player?.play()
            } else {
                layer?.player?.pause()
            }
        }
    )
}


