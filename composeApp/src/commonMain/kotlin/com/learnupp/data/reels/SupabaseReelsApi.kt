package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class SupabaseReelsApi(
    private val client: SupabaseClient,
) : ReelsApi {
    override suspend fun fetchReels(page: Int, pageSize: Int): List<Reel> {
        return fetchReelsInternal(null, page, pageSize)
    }

    override suspend fun fetchUserReels(userId: String, page: Int, pageSize: Int): List<Reel> {
        return fetchReelsInternal(userId, page, pageSize)
    }

    private suspend fun fetchReelsInternal(userId: String?, page: Int, pageSize: Int): List<Reel> {
        return try {
            val offset = (page * pageSize).toLong()
            val end = offset + pageSize - 1

            client.from("reels").select(
                // UPDATED QUERY: Fetch Author AND Course
                columns = Columns.raw("*, profiles(*), courses(*)")
            ) {
                if (userId != null) {
                    filter { eq("user_id", userId) }
                }
                range(offset, end)
                order("created_at", order = Order.DESCENDING)
            }.decodeList<Reel>()

        } catch (t: Throwable) {
            emptyList()
        }
    }
}