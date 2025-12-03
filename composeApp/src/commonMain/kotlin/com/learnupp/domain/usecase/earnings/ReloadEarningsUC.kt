package com.learnupp.domain.usecase.earnings

import com.learnupp.domain.repo.EarningsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadEarningsUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadEarningsUseCaseImpl(
    private val repository: EarningsRepository
) : ReloadEarningsUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

