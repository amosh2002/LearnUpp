package com.learnupp.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.CoreGraphics.CGRectMake
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
            val view = UIView()
            val player = NSURL.URLWithString(url)?.let { AVPlayer(uRL = it) }
            val layer = AVPlayerLayer.playerLayerWithPlayer(player)
            layer.frame = CGRectMake(0.0, 0.0, 0.0, 0.0)
            view.layer.addSublayer(layer)
            view
        },
        update = { view ->
            val layer = view.layer.sublayers?.first() as? AVPlayerLayer
            if (playWhenReady) {
                layer?.player?.play()
            } else {
                layer?.player?.pause()
            }
        }
    )
}


