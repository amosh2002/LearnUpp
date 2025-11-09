package com.learnupp

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import com.learnupp.util.IOSPreferencesManager
import com.learnupp.util.LocalAppComponent
import com.learnupp.util.LocalizationService
import com.learnupp.util.PreferencesManager
import com.learnupp.util.dataStore

fun MainViewController() = ComposeUIViewController {
    val preferencesManager: PreferencesManager = IOSPreferencesManager(dataStore())
    val localizationService = LocalizationService()

    CompositionLocalProvider(LocalAppComponent provides LocalUIViewController.current) {
        App(preferencesManager, localizationService)
    }
    App(preferencesManager, localizationService)
}