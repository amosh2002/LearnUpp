package com.learnupp.data.reels

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Reel
import com.learnupp.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorReelsApi(
    private val client: HttpClient,
) : ReelsApi {
    override suspend fun fetchReels(page: Int, pageSize: Int): List<Reel> {
        Logger.d("ReelsApi", "GET /reels page=$page pageSize=$pageSize")
        return try {
            val reels = client.get("$apiBaseUrl/reels") {
                parameter("page", page)
                parameter("pageSize", pageSize)
            }.body<List<Reel>>()
            Logger.d("ReelsApi", "GET /reels success count=${reels.size}")
            reels
        } catch (t: Throwable) {
            Logger.e("ReelsApi", "GET /reels failed: ${t.message}")
            throw t
        }
    }
}


