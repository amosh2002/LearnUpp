package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreMyProfileReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreMyProfileReelsUseCaseImpl(
    private val repo: ReelsRepository
) : LoadMoreMyProfileReelsUseCase() {
    override suspend fun invoke() {
        repo.loadMoreMyProfile()
    }
}
