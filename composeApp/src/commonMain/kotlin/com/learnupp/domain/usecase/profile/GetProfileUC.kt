package com.learnupp.domain.usecase.profile

import com.learnupp.domain.model.Profile
import com.learnupp.domain.repo.ProfileRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetProfileUseCase : ParameterlessUseCase<Flow<Profile>>()

class GetProfileUseCaseImpl(
    private val repository: ProfileRepository
) : GetProfileUseCase() {
    override fun invoke(): Flow<Profile> = repository.getProfile()
}

