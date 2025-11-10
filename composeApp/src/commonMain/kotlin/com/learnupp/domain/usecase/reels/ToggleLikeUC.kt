package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ToggleReelLikeUseCase : ParameterlessSuspendUseCase<Unit>() {
    abstract suspend operator fun invoke(reelId: String)
}

class ToggleReelLikeUseCaseImpl(
    private val repository: ReelsRepository
) : ToggleReelLikeUseCase() {
    override suspend fun invoke() {
        error("Call invoke(reelId)")
    }

    override suspend fun invoke(reelId: String) {
        repository.toggleReelLike(reelId)
    }
}


