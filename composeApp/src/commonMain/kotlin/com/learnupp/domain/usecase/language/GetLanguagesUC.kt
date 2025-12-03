package com.learnupp.domain.usecase.language

import com.learnupp.domain.model.settings.LanguageOption
import com.learnupp.domain.repo.LanguageOptionsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetLanguagesUseCase : ParameterlessUseCase<Flow<List<LanguageOption>>>()

class GetLanguagesUseCaseImpl(
    private val repository: LanguageOptionsRepository
) : GetLanguagesUseCase() {
    override fun invoke(): Flow<List<LanguageOption>> = repository.getLanguages()
}

