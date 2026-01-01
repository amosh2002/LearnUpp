package com.learnupp.server.auth

import com.learnupp.server.db.RefreshTokensTable
import com.learnupp.server.db.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID

class DbAuthStore(
    private val db: Database,
    private val refreshTtlSeconds: Long = 30L * 24 * 60 * 60,
) : AuthStore {

    override fun createUser(email: String, username: String?, fullName: String?): AuthUserRecord =
        transaction(db) {
            val emailLower = email.trim().lowercase()
            if (UsersTable.selectAll().where { UsersTable.email eq emailLower }.any()) {
                error("USER_EXISTS")
            }
            if (!username.isNullOrBlank() && UsersTable.selectAll()
                    .where { UsersTable.username eq username }
                    .any()
            ) {
                error("USERNAME_EXISTS")
            }
            val id = UUID.randomUUID().toString()
            val now = Instant.now()
            UsersTable.insert {
                it[UsersTable.id] = id
                it[UsersTable.fullName] = fullName
                it[UsersTable.email] = emailLower
                it[UsersTable.username] = username
                it[UsersTable.avatarUrl] = null
                it[UsersTable.isSignUpComplete] = false
                it[UsersTable.createdAt] = now.epochSecond
            }
            AuthUserRecord(
                id,
                emailLower = emailLower,
                username = username,
                fullName = fullName,
                avatarUrl = null,
                isSignUpComplete = false,
            )
        }

    override fun updateProfile(
        userId: String,
        username: String,
        fullName: String?
    ): AuthUserRecord? =
        transaction(db) {
            val userRow = UsersTable.selectAll().where { UsersTable.id eq userId }.firstOrNull()
                ?: return@transaction null
            val usernameLower = username.trim().lowercase()
            if (UsersTable.selectAll()
                    .where { (UsersTable.username eq usernameLower) and (UsersTable.id neq userId) }
                    .any()
            ) {
                error("USERNAME_EXISTS")
            }
            UsersTable.update({ UsersTable.id eq userId }) {
                it[UsersTable.username] = usernameLower
                it[UsersTable.fullName] = fullName
                it[UsersTable.isSignUpComplete] = true
            }
            UsersTable.selectAll().where { UsersTable.id eq userId }.firstOrNull()?.toAuthUser()
        }

    override fun isUsernameAvailable(username: String, excludeUserId: String?): Boolean =
        transaction(db) {
            val usernameLower = username.trim().lowercase()
            val query = UsersTable.selectAll().where { UsersTable.username eq usernameLower }
            val rows = query.toList()
            rows.isEmpty() || rows.all { it[UsersTable.id] == excludeUserId }
        }

    override fun findUserByEmail(email: String): AuthUserRecord? =
        transaction(db) {
            UsersTable.select { UsersTable.email eq email.trim().lowercase() }
                .limit(1)
                .firstOrNull()
                ?.toAuthUser()
        }

    override fun findUserById(userId: String): AuthUserRecord? =
        transaction(db) {
            UsersTable.select { UsersTable.id eq userId }
                .limit(1)
                .firstOrNull()
                ?.toAuthUser()
        }

    override fun createRefreshSession(userId: String, refreshToken: String): RefreshSession =
        transaction(db) {
            val now = Instant.now()
            val hash = sha256Hex(refreshToken)
            val expires = now.plusSeconds(refreshTtlSeconds)
            val sessionId = UUID.randomUUID().toString()
            RefreshTokensTable.insert {
                it[RefreshTokensTable.sessionId] = sessionId
                it[RefreshTokensTable.userId] = userId
                it[RefreshTokensTable.refreshTokenHash] = hash
                it[RefreshTokensTable.createdAt] = now.epochSecond
                it[RefreshTokensTable.expiresAt] = expires.epochSecond
                it[RefreshTokensTable.revokedAt] = null
                it[RefreshTokensTable.rotatedAt] = null
            }
            RefreshSession(sessionId, userId, hash, expires)
        }

    override fun rotateRefreshToken(
        oldRefreshToken: String,
        newRefreshToken: String
    ): RefreshSession? =
        transaction(db) {
            val now = Instant.now()
            val oldHash = sha256Hex(oldRefreshToken)
            val existing = RefreshTokensTable
                .select { RefreshTokensTable.refreshTokenHash eq oldHash }
                .limit(1)
                .firstOrNull()
                ?.toRefreshSession()
                ?: return@transaction null

            if (existing.expiresAt.isBefore(now)) return@transaction null

            // revoke old
            RefreshTokensTable.update({ RefreshTokensTable.refreshTokenHash eq oldHash }) {
                it[revokedAt] = now.epochSecond
                it[rotatedAt] = now.epochSecond
            }

            // create new
            createRefreshSession(existing.userId, newRefreshToken)
        }

    override fun revokeRefreshToken(refreshToken: String): Boolean =
        transaction(db) {
            val now = Instant.now()
            val hash = sha256Hex(refreshToken)
            val updated =
                RefreshTokensTable.update({ RefreshTokensTable.refreshTokenHash eq hash }) {
                    it[revokedAt] = now.epochSecond
                }
            updated > 0
        }

    override fun validateRefreshToken(refreshToken: String): RefreshSession? =
        transaction(db) {
            val now = Instant.now()
            val hash = sha256Hex(refreshToken)
            val row = RefreshTokensTable
                .select { RefreshTokensTable.refreshTokenHash eq hash }
                .limit(1)
                .firstOrNull()
                ?: return@transaction null

            val sess = row.toRefreshSession()
            if (row[RefreshTokensTable.revokedAt] != null) return@transaction null
            if (sess.expiresAt.isBefore(now)) return@transaction null
            sess
        }

    private fun ResultRow.toAuthUser() = AuthUserRecord(
        id = this[UsersTable.id],
        emailLower = this[UsersTable.email],
        username = this[UsersTable.username],
        fullName = this[UsersTable.fullName],
        avatarUrl = this[UsersTable.avatarUrl],
        isSignUpComplete = this[UsersTable.isSignUpComplete],
    )

    private fun ResultRow.toRefreshSession(): RefreshSession =
        RefreshSession(
            sessionId = this[RefreshTokensTable.sessionId],
            userId = this[RefreshTokensTable.userId],
            refreshTokenHash = this[RefreshTokensTable.refreshTokenHash],
            expiresAt = Instant.ofEpochSecond(this[RefreshTokensTable.expiresAt]),
        )

    private fun sha256Hex(value: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(value.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}


