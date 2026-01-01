package com.learnupp.server.db

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = varchar("id", 64)
    val fullName = varchar("full_name", 128).nullable()
    val email = varchar("email", 128).uniqueIndex()
    val username = varchar("username", 64).uniqueIndex().nullable()
    val avatarUrl = varchar("avatar_url", 512).nullable()
    val isSignUpComplete = bool("is_signup_complete").default(false)
    val createdAt = long("created_at") // epoch seconds

    override val primaryKey = PrimaryKey(id)
}

object RefreshTokensTable : Table("refresh_tokens") {
    val sessionId = varchar("session_id", 64)
    val userId = varchar("user_id", 64).index()
    val refreshTokenHash = varchar("refresh_token_hash", 128).uniqueIndex()
    val createdAt = long("created_at") // epoch seconds
    val expiresAt = long("expires_at") // epoch seconds
    val revokedAt = long("revoked_at").nullable()
    val rotatedAt = long("rotated_at").nullable()

    override val primaryKey = PrimaryKey(sessionId)
}

object OtpCodesTable : Table("otp_codes") {
    val id = varchar("id", 64)
    val email = varchar("email", 128).index()
    val code = varchar("code", 8)
    val createdAt = long("created_at")
    val expiresAt = long("expires_at")
    val consumedAt = long("consumed_at").nullable()

    override val primaryKey = PrimaryKey(id)
}


