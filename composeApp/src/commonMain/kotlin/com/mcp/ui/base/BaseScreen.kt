package com.mcp.ui.base

import cafe.adriel.voyager.core.screen.Screen

abstract class BaseScreen(
    screenName: ScreenNameStrings,
) : Screen {
    override val key: String = screenName.getValue()
}

abstract class BaseFullScreen : Screen


