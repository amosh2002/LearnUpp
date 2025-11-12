package com.learnupp.domain.usecase.videos

import com.learnupp.domain.model.Video
import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetVideosUseCase : ParameterlessUseCase<Flow<List<Video>>>()

class GetVideosUseCaseImpl(
    private val repository: VideosRepository
) : GetVideosUseCase() {
    override fun invoke(): Flow<List<Video>> = repository.getVideos()
}


