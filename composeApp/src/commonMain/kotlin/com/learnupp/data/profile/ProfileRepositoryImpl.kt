package com.learnupp.data.profile

import com.learnupp.domain.model.Profile
import com.learnupp.domain.repo.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class ProfileRepositoryImpl(
    private val storage: ProfileStorage,
    private val api: ProfileApi,
    private val client: SupabaseClient,
) : ProfileRepository {

    private val auth = client.auth
    override fun getProfile(): Flow<Profile> = storage.getProfile().filterNotNull()

    override suspend fun updateProfile(
        username: String?,
        fullName: String?,
        about: String?
    ): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUserOrNull() ?: return@withContext false
                val updates = buildJsonObject {
                    if (!username.isNullOrBlank()) {
                        put("username", username.trim().removePrefix("@"))
                    }
                    if (!fullName.isNullOrBlank()) {
                        put("full_name", fullName)
                    }
                    if (about != null) {
                        put("about", about)
                    }
                }

                client.from("profiles").update(updates) {
                    filter { eq("user_id", currentUser.id) }
                }
                true
            } catch (t: Throwable) {
                false
            }
        }

    override suspend fun refreshData() {
        val profile = api.fetchProfile()
        storage.saveProfile(profile)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getProfile().firstOrNull() == null
    }
}

