package com.learnupp.domain.model

import com.learnupp.domain.model.base.CommonSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Simple domain model representing a short learning reel.
 * For now we only support a thumbnail image and metadata; videoUrl is kept for future integration.
 */
@Serializable
data class Reel(
    val id: String,
    val title: String,
    @SerialName("video_url")
    val videoUrl: String, // Placeholder for future playback integration
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("author_title")
    val authorTitle: String,
    @SerialName("likes_count")
    val likesCount: Int,
    @SerialName("comments_count")
    val commentsCount: Int,
    @SerialName("full_course_id")
    val fullCourseId: String? = null, // Optional: if null, no "View Full Course" button is shown
    @SerialName("description")
    val description: String? = null, // Optional: shown with plus button when there's no course
) : CommonSerializable


