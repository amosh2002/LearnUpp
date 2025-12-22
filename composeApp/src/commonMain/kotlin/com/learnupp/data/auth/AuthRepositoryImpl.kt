package com.learnupp.data.auth

//import com.learnupp.data.camera.CameraStorage
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.util.SessionManager

class AuthRepositoryImpl(
//    private val cameraStorage: CameraStorage,
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Boolean {
        // Call API to get bearer token
        val tokens = authApi.login(email, password)
        return if (tokens != null) {
            SessionManager.setTokens(tokens.accessToken, tokens.refreshToken)
            true
        } else {
            com.learnupp.util.Logger.e("AuthRepository", "login returned null tokens")
            false
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (password != confirmPassword) return false
        val tokens = authApi.register(fullName, email, password)
        return if (tokens != null) {
            SessionManager.setTokens(tokens.accessToken, tokens.refreshToken)
            true
        } else {
            com.learnupp.util.Logger.e("AuthRepository", "register returned null tokens")
            false
        }
    }

    override suspend fun logout(): Boolean {
        val refresh = SessionManager.getRefreshToken()
        val result = authApi.logout(refresh)
        // Always clear local state regardless of server result
        SessionManager.logout()
        return result
    }
}