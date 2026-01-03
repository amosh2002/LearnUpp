package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow

interface ReelsStorage {
    fun getGlobalFeed(): Flow<List<Reel>>
    fun getMyProfileFeed(): Flow<List<Reel>>
    fun getLikedReels(): Flow<Set<String>>
    
    fun saveGlobal(reels: List<Reel>)
    fun appendGlobal(reels: List<Reel>)
    
    fun saveMyProfile(reels: List<Reel>)
    fun appendMyProfile(reels: List<Reel>)
    
    suspend fun toggleReelLike(reelId: String)
    suspend fun shareReel(reelId: String)
}


