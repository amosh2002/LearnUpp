package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadVideosUseCaseImpl(
    private val repository: VideosRepository
) : PreloadVideosUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}


