package com.learnupp.util

/**
 * Cross-platform logger.
 *
 * The API matches the existing one in composeApp so the whole app can keep using
 * `Logger.d/i/w/e(...)` unchanged, but the implementation is now shared with the server.
 */
expect object Logger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String)
    fun i(tag: String, msg: String)
    fun v(tag: String, msg: String)
    fun w(tag: String, msg: String)
}


