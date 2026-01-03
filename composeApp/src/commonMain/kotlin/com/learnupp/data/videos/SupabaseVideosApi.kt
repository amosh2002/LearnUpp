package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import com.learnupp.util.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class SupabaseVideosApi(
    private val client: SupabaseClient,
) : VideosApi {

    // 1. Global Feed
    override suspend fun fetchVideos(page: Int, pageSize: Int): List<Video> {
        return try {
            val range = calculateRange(page, pageSize)

            client.from("videos")
                .select(columns = Columns.raw("*, profiles(*), courses(*)")) {
                    range(range.first, range.second)
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Video>()
        } catch (t: Throwable) {
            Logger.e("VideosApi", "fetchVideos failed: ${t.message}")
            // Return empty list instead of crashing to keep UI stable
            emptyList()
        }
    }

    // 2. Author Specific Feed (Used for 'My Profile' AND 'Other Profiles')
    override suspend fun fetchVideosByAuthor(userId: String, page: Int, pageSize: Int): List<Video> {
        return try {
            val range = calculateRange(page, pageSize)

            client.from("videos")
                .select(columns = Columns.raw("*, profiles(*), courses(*)")) {
                    filter {
                        eq("author_id", userId)
                    }
                    range(range.first, range.second)
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Video>()
        } catch (t: Throwable) {
            Logger.e("VideosApi", "fetchVideosByAuthor failed: ${t.message}")
            emptyList()
        }
    }

    // Helper to calculate Supabase Range (0-9, 10-19, etc)
    private fun calculateRange(page: Int, pageSize: Int): Pair<Long, Long> {
        val fromIndex = (page * pageSize).toLong()
        val toIndex = fromIndex + pageSize - 1
        return fromIndex to toIndex
    }
}