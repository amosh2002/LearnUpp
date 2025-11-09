package com.learnupp.domain.usecase.auth

import com.learnupp.domain.usecase.base.BaseSuspendUseCase
import com.learnupp.domain.repo.AuthRepository

abstract class LoginUseCase : BaseSuspendUseCase<LoginUserParams, Boolean>()

data class LoginUserParams(
    val email: String,
    val password: String
)

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase() {
    override suspend fun invoke(params: LoginUserParams): Boolean {
        return authRepository.login(
            email = params.email,
            password = params.password
        )
    }
}
