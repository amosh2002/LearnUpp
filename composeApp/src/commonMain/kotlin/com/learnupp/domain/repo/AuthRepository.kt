package com.learnupp.domain.repo

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String
    ): Boolean

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean

    suspend fun logout(): Boolean
}