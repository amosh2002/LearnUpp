package com.learnupp.domain.usecase.auth

import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.usecase.base.BaseSuspendUseCase

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

abstract class RegisterUseCase : BaseSuspendUseCase<RegisterUserParams, Boolean>()

data class RegisterUserParams(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val acceptedTerms: Boolean
)

class RegisterUseCaseImpl(
    private val authRepository: AuthRepository
) : RegisterUseCase() {
    override suspend fun invoke(params: RegisterUserParams): Boolean {
        if (!params.acceptedTerms) return false
        if (params.password != params.confirmPassword) return false
        return authRepository.register(
            fullName = params.fullName,
            email = params.email,
            password = params.password,
            confirmPassword = params.confirmPassword
        )
    }
}
