package com.learnupp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.learnupp.util.AppContextHolder
import com.learnupp.util.LocalAppComponent
import com.learnupp.util.LocalizationService
import com.learnupp.util.PreferencesManager
import org.koin.mp.KoinPlatform.getKoin

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.handleDeeplinks

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Init application context holder for utilities like share sheets
        AppContextHolder.appContext = applicationContext

        // Retrieve PermissionsService (after delegates are in Koin)
        val preferencesManager: PreferencesManager = getKoin().get()
        val localizationService: LocalizationService = getKoin().get()
        val supabaseClient: SupabaseClient = getKoin().get()

        // Handle Supabase Deep Links (OAuth callbacks)
        supabaseClient.handleDeeplinks(intent)

        setContent {
            CompositionLocalProvider(LocalAppComponent provides this) {
                App(preferencesManager, localizationService)
            }
        }
    }
}