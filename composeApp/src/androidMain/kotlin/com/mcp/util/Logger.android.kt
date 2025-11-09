package com.mcp.util

import android.util.Log

actual object Logger {
    actual fun d(tag: String, msg: String) {
        if (EnvironmentConfig.isDebug) {
            Log.d(tag, msg)
        }
    }

    actual fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    actual fun i(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    actual fun v(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    actual fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }
}