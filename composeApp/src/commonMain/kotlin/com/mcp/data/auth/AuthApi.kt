package com.mcp.data.auth

import io.ktor.client.HttpClient
import kotlinx.coroutines.delay

interface AuthApi {
    suspend fun login(
        ipAddress: String, port: String,
        username: String, password: String
    ): String?

    suspend fun logout(): Boolean
}

class KtorAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun login(
        ipAddress: String, port: String,
        username: String, password: String
    ): String? {
        delay(300)

        if (username == "user" && password == "pass") {
            return "bearer token_123"
        }

        return null
    }

    override suspend fun logout(): Boolean {
        delay(300)
        return true
    }
}