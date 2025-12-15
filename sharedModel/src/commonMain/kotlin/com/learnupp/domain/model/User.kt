package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val email: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
)


