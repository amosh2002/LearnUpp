package com.learnupp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import com.learnupp.di.sharedModules
import com.learnupp.util.LocalAppComponent
import com.learnupp.util.LocalizationService
import com.learnupp.util.PreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Retrieve PermissionsService (after delegates are in Koin)
        val preferencesManager: PreferencesManager = getKoin().get()
        val localizationService: LocalizationService = getKoin().get()

        setContent {
            CompositionLocalProvider(LocalAppComponent provides this) {
                App(preferencesManager, localizationService)
            }
        }
    }
}