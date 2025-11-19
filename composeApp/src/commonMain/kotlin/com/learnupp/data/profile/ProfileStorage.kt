package com.learnupp.data.profile

import com.learnupp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileStorage {
    fun getProfile(): Flow<Profile?>
    suspend fun saveProfile(profile: Profile)
    suspend fun updateAbout(about: String)
}

