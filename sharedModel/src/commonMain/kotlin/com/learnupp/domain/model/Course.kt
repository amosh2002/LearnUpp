package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val title: String,
    val category: String? = "General",
    val rating: Double = 0.0,

    @SerialName("rating_count")
    val ratingCount: Int = 0,

    @SerialName("lectures_count")
    val lecturesCount: Int = 0,

    @SerialName("duration_text")
    val durationText: String? = null,

    // Mapped from 'current_price' column in DB
    @SerialName("current_price")
    val currentPrice: Double = 0.0,

    @SerialName("original_price")
    val originalPrice: Double? = null,

    @SerialName("discount_percent")
    val discountPercent: Int? = null,

    @SerialName("preview_image_url")
    val previewImageUrl: String? = null,

    // 🔗 RELATIONAL DATA
    // Maps the 'instructor_id' foreign key to the Profile object
    @SerialName("profiles")
    val instructor: Profile? = null
) {
    // Helpers
    val instructorName: String
        get() = instructor?.fullName ?: "Unknown Instructor"

    val instructorAvatar: String?
        get() = instructor?.avatarUrl
}