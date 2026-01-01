package com.learnupp.server.auth

import com.learnupp.server.db.OtpCodesTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

data class OtpVerificationResult(
    val emailLower: String,
    val isValid: Boolean,
)

class OtpService(
    private val db: org.jetbrains.exposed.sql.Database,
    private val ttlSeconds: Long = 10 * 60, // 10 minutes
) {

    fun createCode(email: String): String {
        val emailLower = email.trim().lowercase()
        val code = Random.nextInt(10000, 99999).toString().padStart(5, '0')
        val now = Instant.now()
        val expires = now.plusSeconds(ttlSeconds)

        transaction(db) {
            OtpCodesTable.insert {
                it[id] = UUID.randomUUID().toString()
                it[OtpCodesTable.email] = emailLower
                it[OtpCodesTable.code] = code
                it[createdAt] = now.epochSecond
                it[expiresAt] = expires.epochSecond
                it[consumedAt] = null
            }
        }
        println("OTP generated for $emailLower: $code (expires at $expires)")
        return code
    }

    fun verify(email: String, code: String): OtpVerificationResult {
        val emailLower = email.trim().lowercase()
        val now = Instant.now()
        val trimmedCode = code.trim()
        val record = transaction(db) {
            OtpCodesTable.selectAll()
                .where { (OtpCodesTable.email eq emailLower) and (OtpCodesTable.code eq trimmedCode) }
                .orderBy(OtpCodesTable.createdAt, SortOrder.DESC)
                .limit(1)
                .firstOrNull()
        } ?: return OtpVerificationResult(emailLower, false)

        val expiresAt = Instant.ofEpochSecond(record[OtpCodesTable.expiresAt])
        val consumedAtInstant = record[OtpCodesTable.consumedAt]?.let { Instant.ofEpochSecond(it) }
        if (consumedAtInstant != null || expiresAt.isBefore(now)) {
            return OtpVerificationResult(emailLower, false)
        }

        // mark consumed
        transaction(db) {
            OtpCodesTable.update({ OtpCodesTable.id eq record[OtpCodesTable.id] }) {
                it[OtpCodesTable.consumedAt] = now.epochSecond
            }
        }
        return OtpVerificationResult(emailLower, true)
    }
}

