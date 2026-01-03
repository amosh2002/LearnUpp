package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreGlobalReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreGlobalReelsUseCaseImpl(
    private val repo: ReelsRepository
) : LoadMoreGlobalReelsUseCase() {
    override suspend fun invoke() {
        repo.loadMoreGlobal()
    }
}
