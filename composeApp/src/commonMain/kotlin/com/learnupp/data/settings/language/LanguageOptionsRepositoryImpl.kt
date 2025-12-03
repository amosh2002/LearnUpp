package com.learnupp.data.settings.language

import com.learnupp.domain.repo.LanguageOptionsRepository
import com.learnupp.util.LocalizationService
import kotlinx.coroutines.flow.Flow

class LanguageOptionsRepositoryImpl(
    private val api: LanguageOptionsApi,
    private val storage: LanguageOptionsStorage,
    private val localizationService: LocalizationService
) : LanguageOptionsRepository {
    override fun getLanguages(): Flow<List<com.learnupp.domain.model.settings.LanguageOption>> =
        storage.getLanguages()

    override suspend fun refreshData() {
        storage.save(api.fetchLanguages())
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getLanguages().value.isEmpty()
    }

    override suspend fun selectLanguage(code: String) {
        localizationService.applyLanguage(code)
        val updated = storage.getLanguages().value.map {
            it.copy(selected = it.code == code)
        }
        storage.save(updated)
    }
}

