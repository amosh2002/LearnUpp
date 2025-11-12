package com.learnupp.util

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual fun openShareSheet(text: String, url: String?) {
    val message = if (url.isNullOrBlank()) text else "$text\n$url"
    val activityVC = UIActivityViewController(activityItems = listOf(message) as List<Any>, applicationActivities = null)

    val keyWindow = UIApplication.sharedApplication.keyWindow
    val rootController = keyWindow?.rootViewController
    rootController?.presentViewController(activityVC, animated = true, completion = null)
}


