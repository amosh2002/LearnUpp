package com.learnupp.domain.usecase.videos

import com.learnupp.domain.model.Video
import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetGlobalVideosUseCase : ParameterlessUseCase<Flow<List<Video>>>()

class GetGlobalVideosUseCaseImpl(
    private val repo: VideosRepository
) : GetGlobalVideosUseCase() {
    override fun invoke(): Flow<List<Video>> = repo.getGlobalFeed()
}

