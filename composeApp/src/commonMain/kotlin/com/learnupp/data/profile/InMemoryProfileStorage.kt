package com.learnupp.data.profile

import com.learnupp.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryProfileStorage : ProfileStorage {
    private val profileState = MutableStateFlow<Profile?>(null)

    override fun getProfile(): Flow<Profile?> = profileState.asStateFlow()

    override suspend fun saveProfile(profile: Profile) {
        profileState.value = profile
    }

    override suspend fun updateAbout(about: String) {
        profileState.value = profileState.value?.copy(about = about)
    }
}

