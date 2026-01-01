package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val title: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("is_lecturer")
    val isLecturer: Boolean,
    val username: String? = null,
    @SerialName("is_signup_complete")
    val isSignUpComplete: Boolean = false,
    val email: String? = null,
    @SerialName("primary_login")
    val primaryLogin: String? = null, // "email", "google", "apple"
    @SerialName("linked_providers")
    val linkedProviders: List<String> = emptyList(), // e.g., ["email","google"]
    val students: Int = 0,
    val courses: Int = 0,
    val rating: Double = 0.0,
    val followers: Int = 0,
    val following: Int = 0,
    val posts: Int = 0,
    val about: String = "",
) {
    companion object {
        fun empty() = Profile(
            id = "",
            fullName = "",
            title = "",
            isLecturer = false,
            username = null,
            isSignUpComplete = false
        )
    }
}


