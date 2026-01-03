package com.learnupp.domain.repo

import com.learnupp.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : BaseRepository {
    fun getProfile(): Flow<Profile>
    suspend fun updateProfile(username: String?, fullName: String?, about: String?): Boolean
}

