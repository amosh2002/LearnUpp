package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ToggleVideoLikeUseCase : ParameterlessSuspendUseCase<Unit>() {
    abstract suspend operator fun invoke(videoId: String)
}

class ToggleVideoLikeUseCaseImpl(
    private val repository: VideosRepository
) : ToggleVideoLikeUseCase() {
    override suspend fun invoke() {
        error("Call invoke(videoId)")
    }

    override suspend fun invoke(videoId: String) {
        repository.toggleVideoLike(videoId)
    }
}



