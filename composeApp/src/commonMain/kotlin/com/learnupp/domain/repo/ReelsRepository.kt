package com.learnupp.domain.repo

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow

interface ReelsRepository : BaseRepository {
    // Global Feed
    fun getGlobalFeed(): Flow<List<Reel>>
    suspend fun loadMoreGlobal()

    // My Profile Feed
    fun getMyProfileFeed(): Flow<List<Reel>>
    suspend fun refreshMyProfile()
    suspend fun loadMoreMyProfile()

    // Other Users (Stateless)
    suspend fun fetchUserReels(userId: String, page: Int, pageSize: Int): List<Reel>

    // Shared
    fun getLikedReels(): Flow<Set<String>>
    suspend fun toggleReelLike(reelId: String)
    suspend fun shareReel(reelId: String)
}


