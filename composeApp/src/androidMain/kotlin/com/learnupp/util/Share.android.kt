package com.learnupp.util

import android.content.Context
import android.content.Intent

actual fun openShareSheet(text: String, url: String?) {
    // Acquire application context through a static holder
    val ctx = AppContextHolder.appContext ?: return
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, if (url.isNullOrBlank()) text else "$text\n$url")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(shareIntent)
}

object AppContextHolder {
    var appContext: Context? = null
}


