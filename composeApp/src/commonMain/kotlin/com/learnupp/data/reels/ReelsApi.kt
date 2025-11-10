package com.learnupp.data.reels

import com.learnupp.domain.model.Reel

/**
 * API surface for loading reels from backend.
 * We will use a mock implementation for now.
 */
interface ReelsApi {
    // Paged fetch, page is 0-based
    suspend fun fetchReels(page: Int, pageSize: Int): List<Reel>
}


