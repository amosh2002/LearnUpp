package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreMyProfileVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreMyProfileVideosUseCaseImpl(
    private val repo: VideosRepository
) : LoadMoreMyProfileVideosUseCase() {
    override suspend fun invoke() {
        repo.loadMoreMyProfile()
    }
}

