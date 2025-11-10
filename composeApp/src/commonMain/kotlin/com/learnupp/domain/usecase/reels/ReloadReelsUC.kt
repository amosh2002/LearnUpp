package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadReelsUseCaseImpl(
    private val repository: ReelsRepository
) : ReloadReelsUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}


