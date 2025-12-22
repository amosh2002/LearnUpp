package com.learnupp.server.db

import org.jetbrains.exposed.sql.Table

object RevokedTokensTable : Table("revoked_tokens") {
    val jti = varchar("jti", 128).uniqueIndex()
    val userId = varchar("user_id", 64).index()
    val expiresAt = long("expires_at") // epoch seconds
    val createdAt = long("created_at") // epoch seconds

    override val primaryKey = PrimaryKey(jti)
}

