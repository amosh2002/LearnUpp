package com.mcp.data.auth

import com.mcp.data.camera.CameraStorage
import com.mcp.domain.repo.AuthRepository
import com.mcp.util.SessionManager

class AuthRepositoryImpl(
    private val cameraStorage: CameraStorage,
    private val authApi: AuthApi,
) : AuthRepository {
    override suspend fun login(
        ipAddress: String,
        port: String,
        username: String,
        password: String
    ): Boolean {
        // Call API to get bearer token
        val bearerToken = authApi.login(
            ipAddress, port,
            username, password)

        // Save token in session manager and return true if login was successful
        if (!bearerToken.isNullOrBlank()) {
            SessionManager.setBearerToken(bearerToken)
            return true
        }

        // Login failed
        return false
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
            cameraStorage.clearAllData()
            // Remove data from phone
            SessionManager.logout()
        }
        return result
    }
}