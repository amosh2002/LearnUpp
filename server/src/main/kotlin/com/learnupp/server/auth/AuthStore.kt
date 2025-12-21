package com.learnupp.server.auth

interface AuthStore {
    fun createUser(fullName: String, email: String, passwordHash: String): AuthUserRecord
    fun findUserByEmail(email: String): AuthUserRecord?
    fun findUserById(userId: String): AuthUserRecord?

    fun createRefreshSession(userId: String, refreshToken: String): RefreshSession
    fun rotateRefreshToken(oldRefreshToken: String, newRefreshToken: String): RefreshSession?
    fun revokeRefreshToken(refreshToken: String): Boolean
    fun validateRefreshToken(refreshToken: String): RefreshSession?
}

data class AuthUserRecord(
    val id: String,
    val fullName: String,
    val emailLower: String,
    val passwordHash: String,
)

data class RefreshSession(
    val sessionId: String,
    val userId: String,
    val refreshTokenHash: String,
    val expiresAt: java.time.Instant,
)


