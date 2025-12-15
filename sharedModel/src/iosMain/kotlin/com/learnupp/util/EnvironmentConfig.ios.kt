package com.learnupp.util

actual object EnvironmentConfig {
    // Keep the same behavior you had before: default to false unless you implement detection.
    actual val isDebug: Boolean = ENVIRONMENT.IS_DEBUG_BINARY
}

object ENVIRONMENT {
    val IS_DEBUG_BINARY: Boolean = isInDebug()

    private fun isInDebug(): Boolean = false
}


