package com.learnupp.server.auth

import com.learnupp.domain.model.User
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Dev-only storage to unblock client/server integration without a database.
 *
 * Production note: replace with Postgres + Exposed.
 */
class InMemoryAuthStore(
    private val refreshTtlSeconds: Long = 30L * 24 * 60 * 60, // 30 days
) {
    data class UserRecord(
        val user: User,
        val emailLower: String,
        val passwordHash: String,
    )

    data class RefreshSession(
        val sessionId: String,
        val userId: String,
        val refreshTokenHash: String,
        val createdAt: Instant,
        val expiresAt: Instant,
        val revokedAt: Instant? = null,
        val rotatedAt: Instant? = null,
    )

    private val usersByEmail = ConcurrentHashMap<String, UserRecord>()
    private val usersById = ConcurrentHashMap<String, UserRecord>()
    private val sessionsByTokenHash = ConcurrentHashMap<String, RefreshSession>()

    fun createUser(fullName: String, email: String, passwordHash: String): UserRecord {
        val emailLower = email.trim().lowercase()
        require(emailLower.isNotBlank())

        if (usersByEmail.containsKey(emailLower)) error("USER_EXISTS")

        val id = UUID.randomUUID().toString()
        val user = User(id = id, fullName = fullName, email = emailLower, avatarUrl = null)
        val record = UserRecord(user = user, emailLower = emailLower, passwordHash = passwordHash)

        usersByEmail[emailLower] = record
        usersById[id] = record
        return record
    }

    fun findUserByEmail(email: String): UserRecord? =
        usersByEmail[email.trim().lowercase()]

    fun findUserById(userId: String): UserRecord? =
        usersById[userId]

    fun createRefreshSession(userId: String, refreshToken: String): RefreshSession {
        val now = Instant.now()
        val hash = sha256Hex(refreshToken)
        val session = RefreshSession(
            sessionId = UUID.randomUUID().toString(),
            userId = userId,
            refreshTokenHash = hash,
            createdAt = now,
            expiresAt = now.plusSeconds(refreshTtlSeconds)
        )
        sessionsByTokenHash[hash] = session
        return session
    }

    /**
     * Rotation:
     * - If token exists and is valid, revoke it and create a new session for the new token.
     * - If it doesn't exist, treat as invalid (in production we'd also do reuse detection).
     */
    fun rotateRefreshToken(oldRefreshToken: String, newRefreshToken: String): RefreshSession? {
        val now = Instant.now()
        val oldHash = sha256Hex(oldRefreshToken)
        val existing = sessionsByTokenHash[oldHash] ?: return null

        if (existing.revokedAt != null) return null
        if (existing.expiresAt.isBefore(now)) return null

        // revoke old
        sessionsByTokenHash[oldHash] = existing.copy(revokedAt = now, rotatedAt = now)

        // create new
        return createRefreshSession(existing.userId, newRefreshToken)
    }

    fun revokeRefreshToken(refreshToken: String): Boolean {
        val now = Instant.now()
        val hash = sha256Hex(refreshToken)
        val existing = sessionsByTokenHash[hash] ?: return false
        if (existing.revokedAt != null) return true
        sessionsByTokenHash[hash] = existing.copy(revokedAt = now)
        return true
    }

    fun validateRefreshToken(refreshToken: String): RefreshSession? {
        val now = Instant.now()
        val hash = sha256Hex(refreshToken)
        val session = sessionsByTokenHash[hash] ?: return null
        if (session.revokedAt != null) return null
        if (session.expiresAt.isBefore(now)) return null
        return session
    }

    private fun sha256Hex(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(value.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}


