package com.mcp.util

import com.mcp.BuildConfig

actual object EnvironmentConfig {
    actual val isDebug: Boolean = BuildConfig.DEBUG
}