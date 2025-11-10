package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow

interface ReelsStorage {
    fun getReels(): Flow<List<Reel>>
    fun getLikedReels(): Flow<Set<String>>
    suspend fun saveReels(reels: List<Reel>)
    suspend fun appendReels(reels: List<Reel>)
    suspend fun toggleReelLike(reelId: String)
    suspend fun shareReel(reelId: String)
}


