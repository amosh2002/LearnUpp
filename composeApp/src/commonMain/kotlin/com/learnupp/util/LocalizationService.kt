package com.learnupp.util

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class LocalizationService {
    fun applyLanguage(iso: String)
    fun getCurrentLanguage(): String
}