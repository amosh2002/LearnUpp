package com.learnupp.data.auth

import com.learnupp.domain.repo.AuthRepository
import com.learnupp.data.auth.VerifyOtpResult
import com.learnupp.data.auth.OtpRequestResult
import com.learnupp.util.Logger
import com.learnupp.util.SessionManager

class AuthRepositoryImpl(
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun requestOtp(email: String): OtpRequestResult =
        authApi.requestOtp(email)

    override suspend fun verifyOtp(email: String, code: String): VerifyOtpResult? {
        val result = authApi.verifyOtp(email, code)
        result?.tokens?.let { tokens ->
            SessionManager.setTokens(tokens.accessToken, tokens.refreshToken)
            SessionManager.setRequiresProfileCompletion(tokens.requiresProfileCompletion)
        }
        return result
    }

    override suspend fun completeProfile(username: String, fullName: String?): Boolean {
        val ok = authApi.completeProfile(username, fullName)
        if (ok) {
            SessionManager.setRequiresProfileCompletion(false)
        }
        return ok
    }

    override suspend fun isUsernameAvailable(username: String): Boolean {
        return authApi.checkUsername(username)
    }

    override suspend fun login(email: String, password: String): Boolean {
        // Deprecated path; passwordless flow only. Return false to indicate unsupported.
        Logger.e("AuthRepository", "Password login is no longer supported.")
        return false
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        Logger.e(
            "AuthRepository",
            "Password registration is no longer supported."
        )
        return false
    }

    override suspend fun logout(): Boolean {
        val refresh = SessionManager.getRefreshToken()
        val result = authApi.logout(refresh)
        // Always clear local state regardless of server result
        SessionManager.logout()
        return result
    }
}