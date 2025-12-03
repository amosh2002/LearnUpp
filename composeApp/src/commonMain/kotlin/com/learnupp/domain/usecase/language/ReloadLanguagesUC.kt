package com.learnupp.domain.usecase.language

import com.learnupp.domain.repo.LanguageOptionsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadLanguagesUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadLanguagesUseCaseImpl(
    private val repository: LanguageOptionsRepository
) : ReloadLanguagesUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

