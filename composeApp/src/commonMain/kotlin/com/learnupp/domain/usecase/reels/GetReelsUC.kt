package com.learnupp.domain.usecase.reels

import com.learnupp.domain.model.Reel
import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetReelsUseCase : ParameterlessUseCase<Flow<List<Reel>>>()

class GetReelsUseCaseImpl(
    private val repository: ReelsRepository
) : GetReelsUseCase() {
    override fun invoke(): Flow<List<Reel>> = repository.getReels()
}


