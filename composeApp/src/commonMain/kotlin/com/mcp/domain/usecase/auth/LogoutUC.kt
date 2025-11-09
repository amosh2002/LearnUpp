package com.mcp.domain.usecase.auth

import com.mcp.domain.repo.AuthRepository
import com.mcp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LogoutUseCase : ParameterlessSuspendUseCase<Boolean>()

class LogoutUseCaseImpl(
    private val repository: AuthRepository
) : LogoutUseCase() {
    override suspend fun invoke(): Boolean {
        return repository.logout()
    }
}
