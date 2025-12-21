package com.learnupp.data.reels

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Reel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorReelsApi(
    private val client: HttpClient,
) : ReelsApi {
    override suspend fun fetchReels(page: Int, pageSize: Int): List<Reel> =
        client.get("$apiBaseUrl/reels") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
}


