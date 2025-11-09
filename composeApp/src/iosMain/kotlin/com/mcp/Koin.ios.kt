package com.mcp

import com.mcp.di.sharedModules
import com.mcp.util.IOSPreferencesManager
import com.mcp.util.LocalizationService
import com.mcp.util.PreferencesManager
import com.mcp.util.dataStore
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoinIos() {
    startKoin {
        modules(
            sharedModules() + listOf(
                module {
                    // iOS-specific
                    single { dataStore() }
                    single<PreferencesManager> { IOSPreferencesManager(get()) }
                    single<LocalizationService> { LocalizationService() }
                }
            ))
    }
}


