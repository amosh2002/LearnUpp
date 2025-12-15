package com.learnupp.util

/**
 * Minimal environment flags usable from shared code.
 * - Android uses the sharedModel library BuildConfig.DEBUG
 * - iOS and JVM default to non-debug unless configured otherwise
 */
expect object EnvironmentConfig {
    val isDebug: Boolean
}


