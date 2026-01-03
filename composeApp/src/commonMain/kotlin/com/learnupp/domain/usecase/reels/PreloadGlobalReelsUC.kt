package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadGlobalReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadGlobalReelsUseCaseImpl(
    private val repo: ReelsRepository
) : PreloadGlobalReelsUseCase() {
    override suspend fun invoke() {
        repo.preloadData()
    }
}
