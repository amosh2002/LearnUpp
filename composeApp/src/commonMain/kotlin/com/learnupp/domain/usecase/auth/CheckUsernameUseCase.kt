package com.learnupp.domain.usecase.auth

import com.learnupp.domain.repo.AuthRepository

interface CheckUsernameUseCase {
    suspend operator fun invoke(username: String): Boolean
}

class CheckUsernameUseCaseImpl(
    private val repo: AuthRepository
) : CheckUsernameUseCase {
    override suspend fun invoke(username: String): Boolean = repo.isUsernameAvailable(username)
}

