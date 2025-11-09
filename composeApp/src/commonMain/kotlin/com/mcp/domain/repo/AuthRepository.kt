package com.mcp.domain.repo

interface AuthRepository {
    suspend fun login(
        ipAddress: String,
        port: String,
        username: String,
        password: String
    ): Boolean

    suspend fun logout(): Boolean
}