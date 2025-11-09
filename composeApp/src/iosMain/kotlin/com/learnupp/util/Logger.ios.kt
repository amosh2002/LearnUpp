package com.learnupp.util

import platform.Foundation.NSLog

actual object Logger {
    actual fun d(tag: String, msg: String) {
        if (EnvironmentConfig.isDebug) {
            NSLog("🐛 DEBUG %s: %s", tag, msg)
        }
    }

    actual fun e(tag: String, msg: String) {
        NSLog("❌ ERROR %s: %s", tag, msg)
    }

    actual fun i(tag: String, msg: String) {
        NSLog("ℹ️ INFO %s: %s", tag, msg)
    }

    actual fun v(tag: String, msg: String) {
        NSLog("📝 VERBOSE %s: %s", tag, msg)
    }

    actual fun w(tag: String, msg: String) {
        NSLog("⚠️ WARNING %s: %s", tag, msg)
    }
}