package com.learnupp.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import java.util.Date
import java.util.UUID

class TokenService(
    private val secret: String,
    private val issuer: String,
    private val audience: String,
    private val accessTtlSeconds: Long = 10 * 60, // 10 minutes
) {
    private val algorithm = Algorithm.HMAC256(secret)
    private val secureRandom = SecureRandom()

    fun generateAccessToken(userId: String): String = generateAccessTokenWithJti(userId).first

    fun generateAccessTokenWithJti(userId: String): Pair<String, String> {
        val now = Instant.now()
        val exp = now.plusSeconds(accessTtlSeconds)
        val jti = UUID.randomUUID().toString()

        val token = JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withSubject(userId)
            .withJWTId(jti)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(exp))
            .sign(algorithm)

        return token to jti
    }

    fun accessExpiresInSec(): Int = accessTtlSeconds.toInt()

    /**
     * Opaque refresh token (not a JWT). The server stores only a hash of it.
     */
    fun generateRefreshToken(): String {
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}


