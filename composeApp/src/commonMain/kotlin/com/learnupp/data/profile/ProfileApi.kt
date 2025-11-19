package com.learnupp.data.profile

import com.learnupp.domain.model.Profile

interface ProfileApi {
    suspend fun fetchProfile(): Profile
}

