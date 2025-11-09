package com.learnupp

import android.app.Application
import com.learnupp.di.sharedModules
import com.learnupp.util.AndroidPreferencesManager
import com.learnupp.util.LocalizationService
import com.learnupp.util.PreferencesManager
import com.learnupp.util.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class LearnUpp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Provide android context for Koin
            androidContext(this@LearnUpp)

            // Combine your shared modules + any Android-specific modules
            modules(
                sharedModules() + listOf(
                    module {
                        single { dataStore(this@LearnUpp) }
                        single<PreferencesManager> { AndroidPreferencesManager(get()) }
                        single<LocalizationService> { LocalizationService(get()) }
                    }
                )
            )
        }
    }
}
