package com.learnupp.domain.usecase.profile

import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadProfileUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadProfileUseCaseImpl(
    private val repository: ProfileRepository
) : ReloadProfileUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

