package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("user_id")
    val userId: String,
    val username: String,
    @SerialName("full_name")
    val fullName: String? = null,
    val email: String? = null,
    val title: String? = null,
    val about: String = "",
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("is_lecturer")
    val isLecturer: Boolean = false,
    val rating: Double = 0.0,
    @SerialName("is_signup_complete")
    val isSignUpComplete: Boolean = false,
    @SerialName("primary_login")
    val primaryLogin: String? = null, // "email", "google", "apple"
    @SerialName("linked_providers")
    val linkedProviders: List<String> = emptyList(), // e.g., ["email","google"]
    @SerialName("students_count")
    val studentsCount: Int = 0,
    @SerialName("followers_count")
    val followersCount: Int = 0,
    @SerialName("following_count")
    val followingCount: Int = 0,
    @SerialName("posts_count")
    val postsCount: Int = 0, // sum of videos_count + reels_count + courses_count
    @SerialName("videos_count")
    val videosCount: Int = 0,
    @SerialName("reels_count")
    val reelsCount: Int = 0,
    @SerialName("courses_count")
    val coursesCount: Int = 0,
) {
    companion object {
        fun empty() = Profile(
            userId = "",
            fullName = "",
            title = "",
            isLecturer = false,
            username = "",
            isSignUpComplete = false
        )
    }
}


