package com.learnupp.data.videos

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Video
import com.learnupp.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorVideosApi(
    private val client: HttpClient,
) : VideosApi {
    override suspend fun fetchVideos(page: Int, pageSize: Int): List<Video> {
        Logger.d("VideosApi", "GET /videos page=$page pageSize=$pageSize")
        return try {
            val videos = client.get("$apiBaseUrl/videos") {
                parameter("page", page)
                parameter("pageSize", pageSize)
            }.body<List<Video>>()
            Logger.d("VideosApi", "GET /videos success count=${videos.size}")
            videos
        } catch (t: Throwable) {
            Logger.e("VideosApi", "GET /videos failed: ${t.message}")
            throw t
        }
    }
}


