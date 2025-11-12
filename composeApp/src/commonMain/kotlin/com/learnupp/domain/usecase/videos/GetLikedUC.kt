package com.learnupp.domain.usecase.videos

import com.learnupp.domain.repo.VideosRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetLikedVideosUseCase : ParameterlessUseCase<Flow<Set<String>>>()

class GetLikedVideosUseCaseImpl(
    private val repository: VideosRepository
) : GetLikedVideosUseCase() {
    override fun invoke(): Flow<Set<String>> = repository.getLikedVideos()
}


