package com.learnupp.domain.model

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val avatarUrl: String? = null,
)


