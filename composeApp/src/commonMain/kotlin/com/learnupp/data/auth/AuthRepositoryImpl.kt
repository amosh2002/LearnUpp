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
            SessionManager.setBearerToken(tokens.accessToken)
            true
        } else {
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
            SessionManager.setBearerToken(tokens.accessToken)
            true
        } else {
            false
        }
    }

    override suspend fun logout(): Boolean {
        val token = SessionManager.getBearerToken()

        if (token.isNullOrEmpty()) {
            // If we have no token, there's nothing to do or we're already logged out.
            return true
        }
        val result = authApi.logout()
        if (result) {
            // Clear data in storages
//            cameraStorage.clearAllData()
            // Remove data from phone
            SessionManager.logout()
        }
        return result
    }
}