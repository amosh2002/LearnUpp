package com.learnupp.util

actual object EnvironmentConfig {
    actual val isDebug: Boolean = ENVIRONMENT.IS_DEBUG_BINARY
}

object ENVIRONMENT {
    val IS_DEBUG_BINARY: Boolean = isInDebug()

    private fun isInDebug(): Boolean = false
}