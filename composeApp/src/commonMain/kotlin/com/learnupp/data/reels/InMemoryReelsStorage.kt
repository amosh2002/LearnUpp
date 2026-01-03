package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryReelsStorage : ReelsStorage {
    private val globalReelsState = MutableStateFlow<List<Reel>>(emptyList())
    private val myProfileReelsState = MutableStateFlow<List<Reel>>(emptyList())
    private val likedState = MutableStateFlow<Set<String>>(emptySet())

    override fun getGlobalFeed(): Flow<List<Reel>> = globalReelsState.asStateFlow()
    override fun getMyProfileFeed(): Flow<List<Reel>> = myProfileReelsState.asStateFlow()
    override fun getLikedReels(): Flow<Set<String>> = likedState.asStateFlow()

    override fun saveGlobal(reels: List<Reel>) {
        globalReelsState.value = reels
    }

    override fun appendGlobal(reels: List<Reel>) {
        globalReelsState.value += reels
    }

    override fun saveMyProfile(reels: List<Reel>) {
        myProfileReelsState.value = reels
    }

    override fun appendMyProfile(reels: List<Reel>) {
        myProfileReelsState.value += reels
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


