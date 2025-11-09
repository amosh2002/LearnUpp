package com.mcp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.mcp.util.IOSPreferencesManager
import com.mcp.util.LocalAppComponent
import com.mcp.util.LocalizationService
import com.mcp.util.PreferencesManager
import com.mcp.util.dataStore

fun MainViewController() = ComposeUIViewController {
    val preferencesManager: PreferencesManager = IOSPreferencesManager(dataStore())
    val localizationService = LocalizationService()

    CompositionLocalProvider(LocalAppComponent provides LocalUIViewController.current) {
        App(preferencesManager, localizationService)
    }
    App(preferencesManager, localizationService)
}