package com.learnupp.server.auth

import com.learnupp.util.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(
    store: AuthStore?,
    tokenService: TokenService,
) {
    route("/auth") {
        post("/register") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<RegisterRequest>()
            val email = req.email.trim()
            val fullName = req.fullName.trim()

            if (email.isBlank() || fullName.isBlank() || req.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Missing fields")
                return@post
            }

            try {
                val passwordHash = PasswordManager.hash(req.password)
                val user = authStore.createUser(fullName = fullName, email = email, passwordHash = passwordHash)

                val refreshToken = tokenService.generateRefreshToken()
                authStore.createRefreshSession(userId = user.id, refreshToken = refreshToken)

                val accessToken = tokenService.generateAccessToken(user.id)
                call.respond(
                    HttpStatusCode.Created,
                    AuthResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresInSec = tokenService.accessExpiresInSec()
                    )
                )
            } catch (t: Throwable) {
                if (t.message == "USER_EXISTS") {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                } else {
                    Logger.e("Auth", "Register failed: ${t.message}")
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            }
        }

        post("/login") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<LoginRequest>()
            val record = authStore.findUserByEmail(req.email)

            if (record == null || !PasswordManager.verify(req.password, record.passwordHash)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val refreshToken = tokenService.generateRefreshToken()
            authStore.createRefreshSession(userId = record.id, refreshToken = refreshToken)
            val accessToken = tokenService.generateAccessToken(record.id)

            call.respond(
                AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    expiresInSec = tokenService.accessExpiresInSec()
                )
            )
        }

        post("/refresh") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<RefreshRequest>()
            val oldToken = req.refreshToken

            // Validate first (so we can return 401 clearly)
            val session = authStore.validateRefreshToken(oldToken)
            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
                return@post
            }

            // Rotate refresh token
            val newRefreshToken = tokenService.generateRefreshToken()
            val newSession = authStore.rotateRefreshToken(oldToken, newRefreshToken)
            if (newSession == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
                return@post
            }

            val accessToken = tokenService.generateAccessToken(session.userId)
            call.respond(
                AuthResponse(
                    accessToken = accessToken,
                    refreshToken = newRefreshToken,
                    expiresInSec = tokenService.accessExpiresInSec()
                )
            )
        }

        post("/logout") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<LogoutRequest>()
            authStore.revokeRefreshToken(req.refreshToken)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


