package com.learnupp.data.profile

import com.learnupp.domain.model.Profile
import com.learnupp.domain.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class ProfileRepositoryImpl(
    private val storage: ProfileStorage,
    private val api: ProfileApi
) : ProfileRepository {

    override fun getProfile(): Flow<Profile> = storage.getProfile().filterNotNull()

    override suspend fun updateAbout(about: String) {
        storage.updateAbout(about)
    }

    override suspend fun refreshData() {
        val profile = api.fetchProfile()
        storage.saveProfile(profile)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getProfile().firstOrNull() == null
    }
}

