package com.mcp

import android.app.Application
import com.mcp.di.sharedModules
import com.mcp.util.AndroidPreferencesManager
import com.mcp.util.LocalizationService
import com.mcp.util.PreferencesManager
import com.mcp.util.dataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MCP : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Provide android context for Koin
            androidContext(this@MCP)

            // Combine your shared modules + any Android-specific modules
            modules(
                sharedModules() + listOf(
                    module {
                        single { dataStore(this@MCP) }
                        single<PreferencesManager> { AndroidPreferencesManager(get()) }
                        single<LocalizationService> { LocalizationService(get()) }
                    }
                )
            )
        }
    }
}
