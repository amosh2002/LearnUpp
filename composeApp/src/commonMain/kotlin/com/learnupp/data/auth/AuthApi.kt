package com.learnupp.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.learnupp.data.apiBaseUrl
import kotlinx.serialization.Serializable
import com.learnupp.util.Logger

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSec: Int,
)

data class RegisterPayload(
    val full_name: String,
    val email: String,
    val password: String,
)

data class LoginPayload(
    val email: String,
    val password: String,
)

interface AuthApi {
    suspend fun login(
        email: String, password: String
    ): AuthTokens?

    suspend fun refresh(refreshToken: String): AuthTokens?

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): AuthTokens?

    suspend fun logout(): Boolean
}

class KtorAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun login(
        email: String, password: String
    ): AuthTokens? {
        return try {
            val response = client.post("$apiBaseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginPayload(email = email, password = password))
            }
            val dto = response.body<AuthResponseDto>()
            AuthTokens(dto.access_token, dto.refresh_token, dto.expires_in_sec)
        } catch (t: Throwable) {
            Logger.e("AuthApi", "login failed: ${t.message}")
            null
        }
    }

    override suspend fun register(fullName: String, email: String, password: String): AuthTokens? {
        return try {
            Logger.i("AuthApi", "register request → email=$email")
            val response = client.post("$apiBaseUrl/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterPayload(full_name = fullName, email = email, password = password))
            }
            val dto = response.body<AuthResponseDto>()
            Logger.i("AuthApi", "register success → access(exp=${dto.expires_in_sec}s)")
            AuthTokens(dto.access_token, dto.refresh_token, dto.expires_in_sec)
        } catch (t: Throwable) {
            Logger.e("AuthApi", "register failed: ${t.message}")
            null
        }
    }

    override suspend fun logout(): Boolean {
        // Optional server call; for now just return true.
        return true
    }

    override suspend fun refresh(refreshToken: String): AuthTokens? {
        return try {
            val response = client.post("$apiBaseUrl/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshPayload(refresh_token = refreshToken))
            }
            val dto = response.body<AuthResponseDto>()
            AuthTokens(dto.access_token, dto.refresh_token, dto.expires_in_sec)
        } catch (t: Throwable) {
            Logger.e("AuthApi", "refresh failed: ${t.message}")
            null
        }
    }
}

@Serializable
private data class AuthResponseDto(
    val access_token: String,
    val refresh_token: String,
    val expires_in_sec: Int
)

@Serializable
private data class RefreshPayload(
    val refresh_token: String
)