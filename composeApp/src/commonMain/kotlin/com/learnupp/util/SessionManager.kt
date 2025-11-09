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

    private var preferencesManager: PreferencesManager? = null

    // Flow to observe bearer token changes
    private val _bearerTokenFlow = MutableStateFlow<String?>(null)
    val bearerTokenFlow: StateFlow<String?> = _bearerTokenFlow.asStateFlow()

    private val _logoutEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvents = _logoutEvents.asSharedFlow()

    suspend fun initialize(preferencesManager: PreferencesManager) {
        this.preferencesManager = preferencesManager

        val bearerToken = preferencesManager.getString(BEARER_TOKEN)

        _bearerTokenFlow.emit(bearerToken)
    }

    suspend fun logout() {
        // 1️⃣  Wipe everything
        preferencesManager?.clear()

        // 2⃣  Clear the bearer token flow
        _bearerTokenFlow.emit(null)

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

    fun isLoggedIn(): Boolean {
        return _bearerTokenFlow.value != null
    }

}

enum class UserRole {
    USER, ADMIN
}
