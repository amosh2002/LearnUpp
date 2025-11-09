package com.learnupp.domain.usecase.auth

import com.learnupp.domain.usecase.base.BaseSuspendUseCase
import com.learnupp.domain.repo.AuthRepository

abstract class LoginUseCase : BaseSuspendUseCase<LoginUserParams, Boolean>()

data class LoginUserParams(
    val ipAddress: String,
    val port: String,
    val username: String,
    val password: String
)

class LoginUseCaseImpl(
    private val authRepository: AuthRepository
) : LoginUseCase() {
    override suspend fun invoke(params: LoginUserParams): Boolean {
        return authRepository.login(
            ipAddress = params.ipAddress,
            port = params.port,
            username = params.username,
            password = params.password
        )
    }
}
