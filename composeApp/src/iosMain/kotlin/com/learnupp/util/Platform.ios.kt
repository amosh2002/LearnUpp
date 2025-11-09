package com.learnupp.util

import platform.UIKit.UIViewController

actual val currentPlatform: Platform
    get() = Platform.iOS

actual typealias PlatformAppComponent = UIViewController