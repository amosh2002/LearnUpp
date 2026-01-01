package com.learnupp.server.auth

interface AuthStore {
    fun createUser(email: String, username: String? = null, fullName: String? = null): AuthUserRecord
    fun updateProfile(userId: String, username: String, fullName: String?): AuthUserRecord?
    fun isUsernameAvailable(username: String, excludeUserId: String? = null): Boolean
    fun findUserByEmail(email: String): AuthUserRecord?
    fun findUserById(userId: String): AuthUserRecord?

    fun createRefreshSession(userId: String, refreshToken: String): RefreshSession
    fun rotateRefreshToken(oldRefreshToken: String, newRefreshToken: String): RefreshSession?
    fun revokeRefreshToken(refreshToken: String): Boolean
    fun validateRefreshToken(refreshToken: String): RefreshSession?
}

data class AuthUserRecord(
    val id: String,
    val emailLower: String,
    val username: String?,
    val fullName: String?,
    val avatarUrl: String? = null,
    val isSignUpComplete: Boolean = false,
)

data class RefreshSession(
    val sessionId: String,
    val userId: String,
    val refreshTokenHash: String,
    val expiresAt: java.time.Instant,
)


