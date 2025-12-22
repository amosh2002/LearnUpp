package com.learnupp.server.auth

import com.learnupp.server.db.RevokedTokensTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class TokenRevocationRepository(private val db: Database) {
    fun revokeAccessToken(jti: String, userId: String, expiresAt: Instant) {
        val now = Instant.now().epochSecond
        transaction(db) {
            RevokedTokensTable.insertIgnore {
                it[RevokedTokensTable.jti] = jti
                it[RevokedTokensTable.userId] = userId
                it[RevokedTokensTable.expiresAt] = expiresAt.epochSecond
                it[RevokedTokensTable.createdAt] = now
            }
        }
    }

    fun isRevoked(jti: String): Boolean =
        transaction(db) {
            RevokedTokensTable
                .select { RevokedTokensTable.jti eq jti }
                .any { row ->
                    val exp = row[RevokedTokensTable.expiresAt]
                    Instant.now().epochSecond < exp
                }
        }
}

