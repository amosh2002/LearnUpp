package com.learnupp.server.auth

import com.learnupp.util.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(
    store: AuthStore?,
    tokenService: TokenService,
    tokenRevocationRepository: TokenRevocationRepository? = null,
    otpService: OtpService? = null,
) {
    route("/auth") {
        post("/request-otp") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val otp = otpService ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "OTP not configured")
            val req = call.receive<RequestOtpRequest>()
            if (req.email.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Email required")
                return@post
            }
            val code = otp.createCode(req.email)
            // For dev we return the code to help testing; in prod, remove this.
            call.respond(RequestOtpResponse(success = true, debugCode = code))
        }

        post("/verify-otp") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val otp = otpService ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "OTP not configured")
            val req = call.receive<VerifyOtpRequest>()
            if (req.email.isBlank() || req.code.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Email and code required")
                return@post
            }
            val verification = otp.verify(req.email, req.code)
            if (!verification.isValid) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid or expired code")
                return@post
            }

            try {
                val userRecord = authStore.findUserByEmail(verification.emailLower)
                    ?: authStore.createUser(email = verification.emailLower, username = null, fullName = null)

                val requiresProfile = userRecord.username.isNullOrBlank() || !userRecord.isSignUpComplete
                val refreshToken = tokenService.generateRefreshToken()
                authStore.createRefreshSession(userId = userRecord.id, refreshToken = refreshToken)
                val (accessToken, _) = tokenService.generateAccessTokenWithJti(userRecord.id)

                call.respond(
                    AuthResponse(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresInSec = tokenService.accessExpiresInSec(),
                        requiresProfileCompletion = requiresProfile,
                        userId = userRecord.id,
                    )
                )
            } catch (t: Throwable) {
                Logger.e("Auth", "verify-otp failed: ${t.message}\n${t.stackTraceToString()}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("error" to "Internal Server Error")
                )
            }
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

        get("/username-available") {
            val username = call.request.queryParameters["username"]?.trim().orEmpty()
            if (username.isBlank()) return@get call.respond(HttpStatusCode.BadRequest, "username required")
            val userId = call.principal<JWTPrincipal>()?.subject
            val available = store?.isUsernameAvailable(username, excludeUserId = userId) ?: false
            call.respond(UsernameAvailabilityResponse(available))
        }

        authenticate("auth-jwt") {
            post("/complete-profile") {
                val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
                val userId = call.principal<JWTPrincipal>()?.subject
                    ?: return@post call.respond(HttpStatusCode.Unauthorized, "Missing user")
                val req = call.receive<CompleteProfileRequest>()
                val username = req.username.trim().removePrefix("@")
                Logger.d("Auth", "complete-profile attempt userId=$userId username=$username fullName=${req.fullName}")
                if (username.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Username required")
                    return@post
                }
                // Double-check availability server-side to return a clearer error
                val available = authStore.isUsernameAvailable(username, excludeUserId = userId)
                if (!available) {
                    call.respond(HttpStatusCode.Conflict, "Username already taken")
                    return@post
                }
                try {
                    val updated = authStore.updateProfile(userId, username, req.fullName)
                        ?: return@post call.respond(HttpStatusCode.NotFound, "User not found")
                    call.respond(
                        HttpStatusCode.OK,
                        CompleteProfileResponse(
                            ok = true,
                            username = updated.username,
                            fullName = updated.fullName
                        )
                    )
                } catch (t: Throwable) {
                    if (t.message == "USERNAME_EXISTS") {
                        call.respond(HttpStatusCode.Conflict, "Username already taken")
                    } else {
                        Logger.e("Auth", "complete-profile failed: ${t.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                    }
                }
            }
        }

        post("/logout") {
            val authStore = store ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<LogoutRequest>()
            // Revoke refresh token
            authStore.revokeRefreshToken(req.refreshToken)

            // Revoke current access token (if present)
            val principal = call.principal<JWTPrincipal>()
            val jti = principal?.jwtId ?: principal?.payload?.id
            val sub = principal?.subject ?: principal?.payload?.subject
            val exp = principal?.expiresAt?.toInstant()
            if (jti != null && sub != null && exp != null) {
                tokenRevocationRepository?.revokeAccessToken(jti, sub, exp)
            }
            call.respond(HttpStatusCode.NoContent)
        }
    }
}


