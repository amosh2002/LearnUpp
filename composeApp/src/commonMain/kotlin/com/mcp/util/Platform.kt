@file:Suppress("EnumEntryName")

package com.mcp.util

import androidx.compose.runtime.compositionLocalOf

enum class Platform {
    iOS,
    Android
}

expect val currentPlatform: Platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlatformAppComponent

val LocalAppComponent = compositionLocalOf<PlatformAppComponent> { error("No PlatformAppComponent provided") }

fun Platform.toDeviceTypeString(): String = when (this) {
    Platform.Android -> "ANDROID"
    Platform.iOS -> "IOS"
}