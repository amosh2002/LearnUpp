package com.learnupp.server.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpRequest(
    val email: String,
)

@Serializable
data class RequestOtpResponse(
    val success: Boolean,
    @SerialName("debug_code") val debugCode: String? = null,
)

@Serializable
data class VerifyOtpRequest(
    val email: String,
    val code: String,
)

@Serializable
data class CompleteProfileRequest(
    val username: String,
    @SerialName("full_name") val fullName: String? = null,
)

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("expires_in_sec") val expiresInSec: Int,
    @SerialName("requires_profile_completion") val requiresProfileCompletion: Boolean = false,
    @SerialName("user_id") val userId: String? = null,
)

@Serializable
data class UsernameAvailabilityResponse(
    val available: Boolean,
)

@Serializable
data class LogoutRequest(
    @SerialName("refresh_token") val refreshToken: String,
)

@Serializable
data class CompleteProfileResponse(
    val ok: Boolean,
    val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
)


