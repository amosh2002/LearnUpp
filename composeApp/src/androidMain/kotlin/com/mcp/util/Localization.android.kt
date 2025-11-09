package com.mcp.util

import android.content.Context
import android.os.LocaleList
import java.util.Locale

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocalizationService(
    private val context: Context
) {
    actual fun applyLanguage(iso: String) {
        val locale = Locale(iso)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocales(LocaleList(locale))
    }

    actual fun getCurrentLanguage(): String {
        // Return the default language of the system
        return Locale.getDefault().language
    }
}