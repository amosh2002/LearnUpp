package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadReelsUseCaseImpl(
    private val repository: ReelsRepository
) : PreloadReelsUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}


