package com.learnupp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

object SessionManager {
    private const val BEARER_TOKEN = "basasesae23e3dsx#4wdscsccv"
    private const val REFRESH_TOKEN = "basasesae23e3dsx#4wdscsccv_refresh"

    private var preferencesManager: PreferencesManager? = null

    // Flow to observe bearer token changes
    private val _bearerTokenFlow = MutableStateFlow<String?>(null)
    val bearerTokenFlow: StateFlow<String?> = _bearerTokenFlow.asStateFlow()

    private val _refreshTokenFlow = MutableStateFlow<String?>(null)
    val refreshTokenFlow: StateFlow<String?> = _refreshTokenFlow.asStateFlow()

    private val _logoutEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvents = _logoutEvents.asSharedFlow()

    suspend fun initialize(preferencesManager: PreferencesManager) {
        this.preferencesManager = preferencesManager

        val bearerToken = preferencesManager.getString(BEARER_TOKEN)
        val refreshToken = preferencesManager.getString(REFRESH_TOKEN)

        _bearerTokenFlow.emit(bearerToken)
        _refreshTokenFlow.emit(refreshToken)
    }

    suspend fun logout() {
        // 1️⃣  Wipe everything
        preferencesManager?.clear()

        // 2⃣  Clear the bearer token flow
        _bearerTokenFlow.emit(null)
        _refreshTokenFlow.emit(null)

        // 3⃣  Notify the app about logout
        _logoutEvents.tryEmit(Unit)
    }

    suspend fun getBearerToken(): String? {
        return preferencesManager?.getString(BEARER_TOKEN)
    }

    fun getCachedBearerToken(): String? {
        return _bearerTokenFlow.value
    }

    suspend fun setBearerToken(token: String) {
        withContext(Dispatchers.IO) {
            preferencesManager?.saveString(BEARER_TOKEN, token)
        }
        _bearerTokenFlow.emit(token)
    }

    suspend fun setTokens(accessToken: String, refreshToken: String) {
        withContext(Dispatchers.IO) {
            preferencesManager?.saveString(BEARER_TOKEN, accessToken)
            preferencesManager?.saveString(REFRESH_TOKEN, refreshToken)
        }
        _bearerTokenFlow.emit(accessToken)
        _refreshTokenFlow.emit(refreshToken)
    }

    suspend fun getRefreshToken(): String? {
        return preferencesManager?.getString(REFRESH_TOKEN)
    }

    fun getCachedRefreshToken(): String? = _refreshTokenFlow.value

    fun isLoggedIn(): Boolean {
        return _bearerTokenFlow.value != null
    }

}

enum class UserRole {
    USER, ADMIN
}
