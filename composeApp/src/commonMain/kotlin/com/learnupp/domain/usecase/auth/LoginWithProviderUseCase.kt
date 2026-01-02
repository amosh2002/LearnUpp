package com.learnupp.domain.usecase.auth

import com.learnupp.domain.model.AuthProvider
import com.learnupp.domain.repo.AuthRepository

interface LoginWithProviderUseCase {
    suspend operator fun invoke(provider: AuthProvider): Boolean
}

class LoginWithProviderUseCaseImpl(
    private val repo: AuthRepository
) : LoginWithProviderUseCase {
    override suspend fun invoke(provider: AuthProvider): Boolean =
        repo.loginWithProvider(provider)
}

