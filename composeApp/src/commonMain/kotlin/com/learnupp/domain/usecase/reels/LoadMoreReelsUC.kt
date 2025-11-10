package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreReelsUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreReelsUseCaseImpl(
    private val repository: ReelsRepository
) : LoadMoreReelsUseCase() {
    override suspend fun invoke() {
        repository.loadMore()
    }
}


