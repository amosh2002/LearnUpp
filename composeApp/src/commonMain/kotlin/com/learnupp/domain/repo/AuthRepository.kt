package com.learnupp.domain.repo

import com.learnupp.domain.model.AuthProvider
import io.github.jan.supabase.gotrue.user.UserInfo

interface AuthRepository {
    suspend fun currentUserOrNull(): UserInfo?
    suspend fun requestOtp(email: String): com.learnupp.data.auth.OtpRequestResult
    suspend fun verifyOtp(email: String, code: String): com.learnupp.data.auth.VerifyOtpResult?
    suspend fun completeProfile(username: String, fullName: String?): Boolean
    suspend fun isUsernameAvailable(username: String): Boolean
    suspend fun loginWithProvider(provider: AuthProvider): Boolean = false
    suspend fun updateAbout(about: String): Boolean = false
    suspend fun logout(): Boolean
    suspend fun isProfileIncomplete(): Boolean
}