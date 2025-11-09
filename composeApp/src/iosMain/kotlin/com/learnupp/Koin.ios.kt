package com.learnupp

import com.learnupp.di.sharedModules
import com.learnupp.util.IOSPreferencesManager
import com.learnupp.util.LocalizationService
import com.learnupp.util.PreferencesManager
import com.learnupp.util.dataStore
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


