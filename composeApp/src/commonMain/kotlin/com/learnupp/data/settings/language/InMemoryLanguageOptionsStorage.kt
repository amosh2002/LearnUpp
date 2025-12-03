package com.learnupp.data.settings.language

import com.learnupp.domain.model.settings.LanguageOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryLanguageOptionsStorage : LanguageOptionsStorage {
    private val state = MutableStateFlow<List<LanguageOption>>(emptyList())

    override fun getLanguages(): StateFlow<List<LanguageOption>> = state.asStateFlow()

    override suspend fun save(languages: List<LanguageOption>) {
        state.value = languages
    }
}

