package com.learnupp.domain.model

import com.learnupp.domain.model.base.CommonSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: String,
    val title: String,
    @SerialName("instructor_name")
    val instructorName: String,
    @SerialName("preview_image_url")
    val previewImageUrl: String,
    val category: String,
    val rating: Double,
    @SerialName("rating_count")
    val ratingCount: Int,
    @SerialName("lectures_count")
    val lecturesCount: Int,
    @SerialName("duration_text")
    val durationText: String,
    @SerialName("current_price")
    val currentPrice: Double,
    @SerialName("original_price")
    val originalPrice: Double? = null,
    @SerialName("discount_percent")
    val discountPercent: Int? = null
) : CommonSerializable

