package com.learnupp.ui.video

import platform.AVFoundation.AVURLAsset
import platform.Foundation.NSURL

actual fun prefetchVideo(url: String) {
    val asset = AVURLAsset(uRL = NSURL.URLWithString(url))
    asset.loadValuesAsynchronouslyForKeys(listOf("playable")) {
        // No-op; this warms up the resource availability
    }
}


