package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreVideosUseCaseImpl(
    private val repository: VideosRepository
) : LoadMoreVideosUseCase() {
    override suspend fun invoke() {
        repository.loadMore()
    }
}



