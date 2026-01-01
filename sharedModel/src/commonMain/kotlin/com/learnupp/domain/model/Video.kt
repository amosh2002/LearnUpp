package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: String,
    val title: String,
    @SerialName("preview_image_url")
    val previewImageUrl: String,
    @SerialName("duration_sec")
    val durationSec: Int,
    @SerialName("author_name")
    val authorName: String,
    @SerialName("channel_name")
    val channelName: String,
    @SerialName("views_count")
    val viewsCount: Int,
    @SerialName("likes_count")
    val likesCount: Int,
    @SerialName("full_course_id")
    val fullCourseId: String? = null,
    @SerialName("course_url")
    val courseUrl: String? = null,
    @SerialName("full_video_url")
    val fullVideoUrl: String,
)


