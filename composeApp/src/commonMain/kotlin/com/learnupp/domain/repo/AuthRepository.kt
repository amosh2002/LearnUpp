package com.learnupp.domain.repo

import com.learnupp.domain.model.AuthProvider

interface AuthRepository {
    suspend fun requestOtp(email: String): com.learnupp.data.auth.OtpRequestResult
    suspend fun verifyOtp(email: String, code: String): com.learnupp.data.auth.VerifyOtpResult?
    suspend fun completeProfile(username: String, fullName: String?): Boolean
    suspend fun isUsernameAvailable(username: String): Boolean
    suspend fun login(email: String, password: String): Boolean = false
    suspend fun loginWithProvider(provider: AuthProvider): Boolean = false
    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean = false

    suspend fun logout(): Boolean
    suspend fun isProfileIncomplete(): Boolean
}