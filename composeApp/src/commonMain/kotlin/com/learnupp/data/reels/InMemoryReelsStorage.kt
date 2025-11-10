package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryReelsStorage : ReelsStorage {
    private val reelsState = MutableStateFlow<List<Reel>>(emptyList())
    private val likedState = MutableStateFlow<Set<String>>(emptySet())

    override fun getReels(): Flow<List<Reel>> = reelsState.asStateFlow()
    override fun getLikedReels(): Flow<Set<String>> = likedState.asStateFlow()

    override suspend fun saveReels(reels: List<Reel>) {
        reelsState.value = reels
    }

    override suspend fun appendReels(reels: List<Reel>) {
        reelsState.value += reels
    }

    override suspend fun toggleReelLike(reelId: String) {
        likedState.value = likedState.value.let { set ->
            if (set.contains(reelId)) set - reelId else set + reelId
        }
    }

    override suspend fun shareReel(reelId: String) {
        // No-op for now. Could track per-id shares in memory if needed.
    }
}


