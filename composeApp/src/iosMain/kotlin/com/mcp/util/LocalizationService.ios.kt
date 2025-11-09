package com.mcp.util

import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class LocalizationService {
    private val appGroupId = "group.com.mcp"   // <- your group id

    actual fun applyLanguage(iso: String) {
        // 1. Make Compose / system localisation work
        NSUserDefaults.standardUserDefaults
            .setObject(arrayListOf(iso), forKey = "AppleLanguages")

        // 2. Expose the preference to all extensions (notifications, intents…)
        NSUserDefaults(suiteName = appGroupId)
            ?.setObject(iso, forKey = "LANGUAGE")
    }

    actual fun getCurrentLanguage(): String {
        val langs = NSUserDefaults.standardUserDefaults
            .objectForKey("AppleLanguages") as? List<String>
        return langs?.firstOrNull() ?: "eng"
    }
}