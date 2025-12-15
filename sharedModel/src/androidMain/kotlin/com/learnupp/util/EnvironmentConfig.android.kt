package com.learnupp.util

import com.learnupp.shared_model.BuildConfig

actual object EnvironmentConfig {
    actual val isDebug: Boolean = BuildConfig.DEBUG
}


