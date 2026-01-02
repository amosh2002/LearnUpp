package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import com.learnupp.util.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseReelsApi(
    private val client: SupabaseClient,
) : ReelsApi {
    override suspend fun fetchReels(page: Int, pageSize: Int): List<Reel> {
        return try {
            val offset = (page * pageSize).toLong()
            val end = offset + pageSize - 1
            Logger.d("ReelsApi", "Supabase reels page=$page pageSize=$pageSize")

            val reels = client.from("reels")
                .select {
                    range(offset, end)
                }
                .decodeList<Reel>()

            Logger.d("ReelsApi", "Supabase reels success count=${reels.size}")
            reels
        } catch (t: Throwable) {
            Logger.e("ReelsApi", "Supabase reels failed: ${t.message}")
            throw t
        }
    }
}