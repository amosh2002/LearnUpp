package com.learnupp.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.http.contentType
import com.learnupp.data.apiBaseUrl
import kotlinx.serialization.Serializable
import com.learnupp.util.Logger
import io.ktor.client.statement.bodyAsText

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSec: Int,
    val requiresProfileCompletion: Boolean = false,
    val userId: String? = null,
)

data class OtpRequestResult(
    val success: Boolean,
    val debugCode: String? = null,
)

data class VerifyOtpResult(
    val tokens: AuthTokens,
)

@Serializable
private data class RequestOtpPayload(val email: String)

@Serializable
private data class VerifyOtpPayload(val email: String, val code: String)

@Serializable
private data class CompleteProfilePayload(val username: String, val full_name: String? = null)

interface AuthApi {
    suspend fun requestOtp(email: String): OtpRequestResult
    suspend fun verifyOtp(email: String, code: String): VerifyOtpResult?
    suspend fun completeProfile(username: String, fullName: String?): Boolean
    suspend fun checkUsername(username: String): Boolean
    suspend fun refresh(refreshToken: String): AuthTokens?
    suspend fun logout(refreshToken: String?): Boolean
}

class KtorAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun requestOtp(email: String): OtpRequestResult {
        Logger.d("AuthApi", "requestOtp -> POST /auth/request-otp email=$email")
        return try {
            val response = client.post("$apiBaseUrl/auth/request-otp") {
                contentType(ContentType.Application.Json)
                setBody(RequestOtpPayload(email = email))
            }
            val dto = response.body<RequestOtpResponseDto>()
            Logger.d("AuthApi", "requestOtp success debugCode=${dto.debug_code}")
            OtpRequestResult(success = dto.success, debugCode = dto.debug_code)
        } catch (t: Throwable) {
            Logger.e("AuthApi", "requestOtp failed: ${t.message}")
            OtpRequestResult(success = false, debugCode = null)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): VerifyOtpResult? {
        val maskedCode = if (code.length >= 2) "****${code.takeLast(2)}" else code
        Logger.d("AuthApi", "verifyOtp -> POST /auth/verify-otp email=$email code=$maskedCode")
        return try {
            val response = client.post("$apiBaseUrl/auth/verify-otp") {
                contentType(ContentType.Application.Json)
                setBody(VerifyOtpPayload(email = email, code = code))
            }
            if (!response.status.isSuccess()) {
                val errBody = runCatching { response.bodyAsText() }.getOrNull()
                Logger.e(
                    "AuthApi",
                    "verifyOtp non-success status=${response.status.value} body=$errBody"
                )
                return null
            }
            val dto = response.body<AuthResponseDto>()
            Logger.d(
                "AuthApi",
                "verifyOtp success requiresProfileCompletion=${dto.requires_profile_completion} userId=${dto.user_id}"
            )
            VerifyOtpResult(
                tokens = AuthTokens(
                    accessToken = dto.access_token,
                    refreshToken = dto.refresh_token,
                    expiresInSec = dto.expires_in_sec,
                    requiresProfileCompletion = dto.requires_profile_completion,
                    userId = dto.user_id,
                )
            )
        } catch (t: Throwable) {
            Logger.e("AuthApi", "verifyOtp failed: ${t.message}")
            null
        }
    }

    override suspend fun completeProfile(username: String, fullName: String?): Boolean {
        Logger.d("AuthApi", "completeProfile -> POST /auth/complete-profile username=$username fullName=$fullName")
        return try {
            val response = client.post("$apiBaseUrl/auth/complete-profile") {
                contentType(ContentType.Application.Json)
                setBody(CompleteProfilePayload(username = username, full_name = fullName))
            }
            val ok = response.status.value in 200..299
            Logger.d("AuthApi", "completeProfile success=$ok")
            ok
        } catch (t: Throwable) {
            Logger.e("AuthApi", "completeProfile failed: ${t.message}")
            false
        }
    }

    override suspend fun checkUsername(username: String): Boolean {
        Logger.d("AuthApi", "checkUsername -> GET /auth/username-available username=$username")
        return try {
            val response = client.get("$apiBaseUrl/auth/username-available") {
                url.parameters.append("username", username)
            }
            val dto = response.body<UsernameAvailabilityDto>()
            Logger.d("AuthApi", "checkUsername result available=${dto.available}")
            dto.available
        } catch (t: Throwable) {
            Logger.e("AuthApi", "checkUsername failed: ${t.message}")
            false
        }
    }

    override suspend fun logout(refreshToken: String?): Boolean {
        if (refreshToken.isNullOrEmpty()) {
            Logger.e("AuthApi", "logout skipped: no refresh token")
            return false
        }
        Logger.d("AuthApi", "logout -> POST /auth/logout")
        return try {
            val response = client.post("$apiBaseUrl/auth/logout") {
                contentType(ContentType.Application.Json)
                setBody(LogoutPayload(refresh_token = refreshToken))
            }
            val ok = response.status.value in 200..299
            Logger.d("AuthApi", "logout success=$ok")
            ok
        } catch (t: Throwable) {
            Logger.e("AuthApi", "logout failed: ${t.message}")
            false
        }
    }

    override suspend fun refresh(refreshToken: String): AuthTokens? {
        Logger.d("AuthApi", "refresh -> POST /auth/refresh")
        return try {
            val response = client.post("$apiBaseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshPayload(refresh_token = refreshToken))
            }
            val dto = response.body<AuthResponseDto>()
            Logger.d("AuthApi", "refresh success expiresIn=${dto.expires_in_sec}")
            AuthTokens(
                dto.access_token,
                dto.refresh_token,
                dto.expires_in_sec,
                dto.requires_profile_completion,
                dto.user_id
            )
        } catch (t: Throwable) {
            Logger.e("AuthApi", "refresh failed: ${t.message}")
            null
        }
    }
}

@Serializable
private data class RequestOtpResponseDto(
    val success: Boolean,
    val debug_code: String? = null,
)

@Serializable
private data class AuthResponseDto(
    val access_token: String,
    val refresh_token: String,
    val expires_in_sec: Int,
    val requires_profile_completion: Boolean = false,
    val user_id: String? = null,
)

@Serializable
private data class UsernameAvailabilityDto(val available: Boolean)

@Serializable
private data class RefreshPayload(
    val refresh_token: String
)

@Serializable
private data class LogoutPayload(
    val refresh_token: String
)