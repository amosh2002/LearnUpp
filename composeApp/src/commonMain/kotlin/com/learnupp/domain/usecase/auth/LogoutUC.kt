package com.learnupp.domain.usecase.auth

import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LogoutUseCase : ParameterlessSuspendUseCase<Boolean>()

class LogoutUseCaseImpl(
    private val repository: AuthRepository
) : LogoutUseCase() {
    override suspend fun invoke(): Boolean {
        return repository.logout()
    }
}
