package com.learnupp.data.profile

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Profile
import com.learnupp.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorProfileApi(
    private val client: HttpClient,
) : ProfileApi {
    override suspend fun fetchProfile(): Profile {
        Logger.d("ProfileApi", "GET /profile")
        return try {
            val profile = client.get("$apiBaseUrl/profile").body<Profile>()
            Logger.d("ProfileApi", "GET /profile success fullName=${profile.fullName} username=${profile.username}")
            profile
        } catch (t: Throwable) {
            Logger.e("ProfileApi", "GET /profile failed: ${t.message}")
            throw t
        }
    }
}


