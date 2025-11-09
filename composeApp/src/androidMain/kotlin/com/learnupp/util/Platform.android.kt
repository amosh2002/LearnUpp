package com.learnupp.util

import android.app.Activity

actual val currentPlatform: Platform
    get() = Platform.Android

actual typealias PlatformAppComponent = Activity
