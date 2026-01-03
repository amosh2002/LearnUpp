package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadMyProfileVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadMyProfileVideosUseCaseImpl(
    private val repository: VideosRepository
) : PreloadMyProfileVideosUseCase() {
    override suspend fun invoke() {
        repository.preloadMyProfile()
    }
}

