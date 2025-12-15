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
            isLecturer = false
        )
    }
}


