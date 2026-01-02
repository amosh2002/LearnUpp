package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: String,
    val title: String,
    @SerialName("video_url") val videoUrl: String,
    @SerialName("thumbnail_url") val thumbnailUrl: String,
    @SerialName("duration_sec") val durationSec: Int,
    @SerialName("views_count") val viewsCount: Int,
    @SerialName("likes_count") val likesCount: Int,

    // Author Data (Already Done)
    @SerialName("profiles")
    val author: Profile? = null,

    // Keep this just in case you need the raw ID quickly
    @SerialName("full_course_id")
    val fullCourseId: String? = null
) {
    val displayAuthorName: String get() = author?.fullName ?: "Unknown"
    val displayAuthorAvatar: String? get() = author?.avatarUrl

    // Helper to check if this video belongs to a course
    val isCoursePreview: Boolean get() = fullCourseId != null
}