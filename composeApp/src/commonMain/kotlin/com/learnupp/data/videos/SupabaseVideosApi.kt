package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import com.learnupp.util.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseVideosApi(
    private val client: SupabaseClient,
) : VideosApi {
    override suspend fun fetchVideos(page: Int, pageSize: Int): List<Video> {
        return try {
            // Calculate 0-based range
            val fromIndex = (page * pageSize).toLong()
            val toIndex = fromIndex + pageSize - 1

            Logger.d(
                "VideosApi",
                "Supabase videos page=$page pageSize=$pageSize range=$fromIndex..$toIndex"
            )

            val videos = client.from("videos")
                .select {
                    range(fromIndex, toIndex)
                }
                .decodeList<Video>()

            Logger.d("VideosApi", "Supabase videos success count=${videos.size}")
            videos
        } catch (t: Throwable) {
            Logger.e("VideosApi", "Supabase videos failed: ${t.message}")
            throw t
        }
    }
}


