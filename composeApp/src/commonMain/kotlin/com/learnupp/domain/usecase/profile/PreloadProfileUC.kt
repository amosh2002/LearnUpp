package com.learnupp.domain.usecase.profile

import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadProfileUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadProfileUseCaseImpl(
    private val repository: ProfileRepository
) : PreloadProfileUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}

