package com.learnupp.domain.repo

import com.learnupp.domain.model.settings.LanguageOption
import kotlinx.coroutines.flow.Flow

interface LanguageOptionsRepository : BaseRepository {
    fun getLanguages(): Flow<List<LanguageOption>>
    suspend fun selectLanguage(code: String)
}

