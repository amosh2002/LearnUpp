package com.learnupp.data.settings.language

import com.learnupp.domain.model.settings.LanguageOption
import kotlinx.coroutines.flow.StateFlow

interface LanguageOptionsStorage {
    fun getLanguages(): StateFlow<List<LanguageOption>>
    suspend fun save(languages: List<LanguageOption>)
}

