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
    store: InMemoryAuthStore,
    tokenService: TokenService,
) {
    route("/auth") {
        post("/register") {
            val req = call.receive<RegisterRequest>()
            val email = req.email.trim()
            val fullName = req.fullName.trim()

            if (email.isBlank() || fullName.isBlank() || req.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Missing fields")
                return@post
            }

            try {
                val passwordHash = PasswordManager.hash(req.password)
                val user = store.createUser(fullName = fullName, email = email, passwordHash = passwordHash).user

                val refreshToken = tokenService.generateRefreshToken()
                store.createRefreshSession(userId = user.id, refreshToken = refreshToken)

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
            val req = call.receive<LoginRequest>()
            val record = store.findUserByEmail(req.email)

            if (record == null || !PasswordManager.verify(req.password, record.passwordHash)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val refreshToken = tokenService.generateRefreshToken()
            store.createRefreshSession(userId = record.user.id, refreshToken = refreshToken)
            val accessToken = tokenService.generateAccessToken(record.user.id)

            call.respond(
                AuthResponse(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    expiresInSec = tokenService.accessExpiresInSec()
                )
            )
        }

        post("/refresh") {
            val req = call.receive<RefreshRequest>()
            val oldToken = req.refreshToken

            // Validate first (so we can return 401 clearly)
            val session = store.validateRefreshToken(oldToken)
            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
                return@post
            }

            // Rotate refresh token
            val newRefreshToken = tokenService.generateRefreshToken()
            val newSession = store.rotateRefreshToken(oldToken, newRefreshToken)
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
            val req = call.receive<LogoutRequest>()
            store.revokeRefreshToken(req.refreshToken)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


