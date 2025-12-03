package com.learnupp.data.settings.language

import com.learnupp.domain.model.settings.LanguageOption

interface LanguageOptionsApi {
    suspend fun fetchLanguages(): List<LanguageOption>
}

