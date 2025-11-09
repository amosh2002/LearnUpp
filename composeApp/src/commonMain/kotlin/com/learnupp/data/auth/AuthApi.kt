package com.learnupp.data.auth

import io.ktor.client.HttpClient
import kotlinx.coroutines.delay

interface AuthApi {
    suspend fun login(
        email: String, password: String
    ): String?

    suspend fun logout(): Boolean
}

class KtorAuthApi(private val client: HttpClient) : AuthApi {
    override suspend fun login(
        email: String, password: String
    ): String? {
        delay(300)

        if (email == "user" && password == "pass") {
            return "bearer token_123"
        }

        return null
    }

    override suspend fun logout(): Boolean {
        delay(300)
        return true
    }
}