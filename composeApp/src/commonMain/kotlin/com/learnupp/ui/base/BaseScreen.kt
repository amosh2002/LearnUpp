package com.learnupp.ui.base

import cafe.adriel.voyager.core.screen.Screen

abstract class BaseScreen(
    val screenName: ScreenNameStrings,
    val hideTopAppBar: Boolean = false,
    val hideBottomNavBar: Boolean = false,
    val ignoreTopImePadding: Boolean = false,
    val ignoreBottomImePadding: Boolean = false
) : Screen {
    override val key: String = screenName.getValue()
}

