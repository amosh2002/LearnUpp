package com.learnupp.util

expect object Logger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String)
    fun i(tag: String, msg: String)
    fun v(tag: String, msg: String)
    fun w(tag: String, msg: String)
}
