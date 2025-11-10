package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ShareReelUseCase : ParameterlessSuspendUseCase<Unit>() {
    abstract suspend operator fun invoke(reelId: String)
}

class ShareReelUseCaseImpl(
    private val repository: ReelsRepository
) : ShareReelUseCase() {
    override suspend fun invoke() {
        error("Call invoke(reelId)")
    }

    override suspend fun invoke(reelId: String) {
        repository.shareReel(reelId)
    }
}


