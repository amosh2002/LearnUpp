package com.learnupp.server.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.learnupp.server.auth.TokenRevocationRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity(tokenRevocationRepository: TokenRevocationRepository? = null) {
    // Dev defaults; override in production via env vars.
    val secret = System.getenv("JWT_SECRET") ?: "dev_secret_do_not_use_in_prod"
    val issuer = System.getenv("JWT_ISSUER") ?: "https://learnupp.server"
    val audience = System.getenv("JWT_AUDIENCE") ?: "learnupp-client"
    val realm = System.getenv("JWT_REALM") ?: "LearnUpp"

    install(Authentication) {
        jwt("auth-jwt") {
            this.realm = realm
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                val jti = credential.payload.id
                val isAudienceOk = credential.payload.audience.contains(audience)
                val isRevoked = jti != null && tokenRevocationRepository?.isRevoked(jti) == true
                if (isAudienceOk && !isRevoked) JWTPrincipal(credential.payload) else null
            }
        }
    }
}


