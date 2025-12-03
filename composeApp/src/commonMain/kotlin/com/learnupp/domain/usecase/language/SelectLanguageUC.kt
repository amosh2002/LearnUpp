package com.learnupp.domain.usecase.language

import com.learnupp.domain.repo.LanguageOptionsRepository
import com.learnupp.domain.usecase.base.BaseSuspendUseCase

abstract class SelectLanguageUseCase : BaseSuspendUseCase<String, Unit>()

class SelectLanguageUseCaseImpl(
    private val repository: LanguageOptionsRepository
) : SelectLanguageUseCase() {
    override suspend fun invoke(params: String) {
        repository.selectLanguage(params)
    }
}

