package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class RefreshMyProfileVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class RefreshMyProfileVideosUseCaseImpl(
    private val repo: VideosRepository
) : RefreshMyProfileVideosUseCase() {
    override suspend fun invoke() {
        repo.refreshMyProfile()
    }
}

