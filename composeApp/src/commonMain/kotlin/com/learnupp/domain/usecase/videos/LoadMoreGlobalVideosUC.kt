package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreGlobalVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreGlobalVideosUseCaseImpl(
    private val repo: VideosRepository
) : LoadMoreGlobalVideosUseCase() {
    override suspend fun invoke() {
        repo.loadMoreGlobal()
    }
}

