package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadVideosUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadVideosUseCaseImpl(
    private val repository: VideosRepository
) : ReloadVideosUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}



