package com.learnupp.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import com.learnupp.util.LanguageEnum

abstract class BaseScreenModel : ScreenModel {
    protected var currentLanguage: LanguageEnum? = null
    private var currentToken: String? = null

    // Returns true if refresh is needed (language changed)
    fun shouldRefreshBasedOnLanguageChange(language: LanguageEnum): Boolean {
        if (currentLanguage == null) {
            currentLanguage = language
            return false
        }
        return if (language != currentLanguage) {
            currentLanguage = language
            true
        } else {
            false
        }
    }

    // Returns true if refresh is needed (token changed)
    fun shouldRefreshBasedOnTokenChange(token: String?): Boolean {
        if (currentToken == null) {
            currentToken = token
            return false
        }
        return if (token != currentToken) {
            currentToken = token
            true
        } else {
            false
        }
    }
}
