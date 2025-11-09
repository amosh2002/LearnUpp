package com.learnupp.util

import com.learnupp.BuildConfig

actual object EnvironmentConfig {
    actual val isDebug: Boolean = BuildConfig.DEBUG
}