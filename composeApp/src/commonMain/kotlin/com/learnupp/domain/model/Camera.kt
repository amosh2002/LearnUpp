package com.learnupp.domain.model

import com.learnupp.domain.model.base.CommonSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Camera(
    val id: String,
    val name: String,
    @SerialName("is_live")
    val isLive: Boolean,
    @SerialName("preview_image_url")
    val previewImageUrl: String? = null,
) : CommonSerializable