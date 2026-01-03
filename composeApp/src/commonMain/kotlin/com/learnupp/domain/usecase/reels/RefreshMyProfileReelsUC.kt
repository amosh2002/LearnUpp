package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class RefreshMyProfileReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class RefreshMyProfileReelsUseCaseImpl(
    private val repo: ReelsRepository
) : RefreshMyProfileReelsUseCase() {
    override suspend fun invoke() {
        repo.refreshMyProfile()
    }
}
