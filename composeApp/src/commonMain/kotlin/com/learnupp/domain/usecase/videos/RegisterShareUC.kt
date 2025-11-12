package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ShareVideoUseCase : ParameterlessSuspendUseCase<Unit>() {
    abstract suspend operator fun invoke(videoId: String)
}

class ShareVideoUseCaseImpl(
    private val repository: VideosRepository
) : ShareVideoUseCase() {
    override suspend fun invoke() {
        error("Call invoke(videoId)")
    }

    override suspend fun invoke(videoId: String) {
        repository.shareVideo(videoId)
    }
}


