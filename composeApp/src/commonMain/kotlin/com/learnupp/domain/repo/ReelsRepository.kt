package com.learnupp.domain.repo

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow

interface ReelsRepository : BaseRepository {
    // Get reels from local storage
    fun getReels(): Flow<List<Reel>>
    fun getLikedReels(): Flow<Set<String>>

    // Load next page and append to storage
    suspend fun loadMore()

    // Local interactions
    suspend fun toggleReelLike(reelId: String)
    suspend fun shareReel(reelId: String)
}


