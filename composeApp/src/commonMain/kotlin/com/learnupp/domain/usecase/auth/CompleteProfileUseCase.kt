package com.learnupp.domain.usecase.auth

import com.learnupp.domain.repo.AuthRepository

interface CompleteProfileUseCase {
    suspend operator fun invoke(username: String, fullName: String?): Boolean
}

class CompleteProfileUseCaseImpl(
    private val repo: AuthRepository
) : CompleteProfileUseCase {
    override suspend fun invoke(username: String, fullName: String?): Boolean =
        repo.completeProfile(username, fullName)
}

