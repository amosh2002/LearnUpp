package com.learnupp.domain.usecase.reels

import com.learnupp.domain.model.Reel
import com.learnupp.domain.repo.ReelsRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetMyProfileReelsUseCase : ParameterlessUseCase<Flow<List<Reel>>>()

class GetMyProfileReelsUseCaseImpl(
    private val repo: ReelsRepository
) : GetMyProfileReelsUseCase() {
    override fun invoke(): Flow<List<Reel>> = repo.getMyProfileFeed()
}
