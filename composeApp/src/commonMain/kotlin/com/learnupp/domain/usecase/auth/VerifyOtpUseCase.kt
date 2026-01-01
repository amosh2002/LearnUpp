package com.learnupp.domain.usecase.auth

import com.learnupp.data.auth.VerifyOtpResult
import com.learnupp.domain.repo.AuthRepository

interface VerifyOtpUseCase {
    suspend operator fun invoke(email: String, code: String): VerifyOtpResult?
}

class VerifyOtpUseCaseImpl(
    private val repo: AuthRepository
) : VerifyOtpUseCase {
    override suspend fun invoke(email: String, code: String): VerifyOtpResult? = repo.verifyOtp(email, code)
}

