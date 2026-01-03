package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadGlobalVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadGlobalVideosUseCaseImpl(
    private val repository: VideosRepository
) : PreloadGlobalVideosUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}

