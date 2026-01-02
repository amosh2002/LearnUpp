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
    override suspend fun fetchVideos(page: Int, pageSize: Int): List<Video> {
        return try {
            val fromIndex = (page * pageSize).toLong()
            val toIndex = fromIndex + pageSize - 1

            client.from("videos")
                .select(
                    // UPDATED QUERY:
                    // *,           -> All Video fields
                    // profiles(*), -> The Author Object
                    // courses(*)   -> The Course Object (if full_course_id exists)
                    columns = Columns.raw("*, profiles(*), courses(*)")
                ) {
                    range(fromIndex, toIndex)
                    order("created_at", order = Order.DESCENDING)
                }
                .decodeList<Video>()
        } catch (t: Throwable) {
            Logger.e("VideosApi", "Supabase videos failed: ${t.message}")
            throw t
        }
    }
}