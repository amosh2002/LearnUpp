package com.learnupp.domain.usecase.reels

import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetLikedReelsUseCase : ParameterlessUseCase<Flow<Set<String>>>()

class GetLikedReelsUseCaseImpl(
    private val repository: ReelsRepository
) : GetLikedReelsUseCase() {
    override fun invoke(): Flow<Set<String>> = repository.getLikedReels()
}


