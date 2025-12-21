package com.learnupp.data.profile

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Profile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorProfileApi(
    private val client: HttpClient,
) : ProfileApi {
    override suspend fun fetchProfile(): Profile =
        client.get("$apiBaseUrl/profile").body()
}


