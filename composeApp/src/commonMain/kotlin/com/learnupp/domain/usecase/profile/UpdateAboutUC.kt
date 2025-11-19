package com.learnupp.domain.usecase.profile

import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class UpdateProfileAboutUseCase : ParameterlessSuspendUseCase<Unit>() {
    abstract suspend operator fun invoke(about: String)
}

class UpdateProfileAboutUseCaseImpl(
    private val repository: ProfileRepository
) : UpdateProfileAboutUseCase() {
    override suspend fun invoke() {
        error("Call invoke(about)")
    }

    override suspend fun invoke(about: String) {
        repository.updateAbout(about)
    }
}

