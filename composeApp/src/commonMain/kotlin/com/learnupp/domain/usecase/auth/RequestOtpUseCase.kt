package com.learnupp.domain.usecase.auth

import com.learnupp.domain.repo.AuthRepository
import com.learnupp.data.auth.OtpRequestResult

interface RequestOtpUseCase {
    suspend operator fun invoke(email: String): OtpRequestResult
}

class RequestOtpUseCaseImpl(
    private val repo: AuthRepository
) : RequestOtpUseCase {
    override suspend fun invoke(email: String): OtpRequestResult = repo.requestOtp(email)
}

