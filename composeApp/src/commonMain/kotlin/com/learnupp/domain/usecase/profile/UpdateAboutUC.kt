package com.learnupp.domain.usecase.profile

import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.usecase.base.BaseSuspendUseCase

abstract class UpdateProfileAboutUseCase : BaseSuspendUseCase<String, Unit>()

class UpdateProfileAboutUseCaseImpl(
    private val repository: AuthRepository
) : UpdateProfileAboutUseCase() {
    override suspend fun invoke(params: String) {
        repository.updateAbout(params)
    }
}

