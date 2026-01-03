package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadMyProfileReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadMyProfileReelsUseCaseImpl(
    private val repo: ReelsRepository
) : PreloadMyProfileReelsUseCase() {
    override suspend fun invoke() {
        repo.refreshMyProfile()
    }
}
