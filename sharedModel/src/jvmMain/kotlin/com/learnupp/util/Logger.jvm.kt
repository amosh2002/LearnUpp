package com.learnupp.util

actual object Logger {
    private fun out(level: String, tag: String, msg: String) {
        // Simple JVM output with your emoji-style prefixes.
        println("$level $tag: $msg")
    }

    actual fun d(tag: String, msg: String) {
        if (EnvironmentConfig.isDebug) out("🐛 DEBUG", tag, msg)
    }

    actual fun e(tag: String, msg: String) {
        out("❌ ERROR", tag, msg)
    }

    actual fun i(tag: String, msg: String) {
        out("ℹ️ INFO", tag, msg)
    }

    actual fun v(tag: String, msg: String) {
        out("📝 VERBOSE", tag, msg)
    }

    actual fun w(tag: String, msg: String) {
        out("⚠️ WARNING", tag, msg)
    }
}


