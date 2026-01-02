package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reel(
    val id: String,
    val title: String? = null,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("likes_count") val likesCount: Int = 0,
    @SerialName("comments_count") val commentsCount: Int = 0,
    val description: String? = null,

    // Author Data
    @SerialName("profiles")
    val author: Profile? = null,

    @SerialName("full_course_id")
    val fullCourseId: String? = null
) {
    val authorName: String get() = author?.fullName ?: "Unknown"
    val authorAvatar: String? get() = author?.avatarUrl
    val isCoursePreview: Boolean get() = fullCourseId != null
}