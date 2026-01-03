package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.repo.ReelsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class ReelsRepositoryImpl(
    private val storage: ReelsStorage,
    private val api: ReelsApi,
    private val authRepository: AuthRepository
) : ReelsRepository {
    private val pageSize = 6
    
    private var globalPage = -1
    private var myProfilePage = -1

    // Global Feed
    override fun getGlobalFeed(): Flow<List<Reel>> = storage.getGlobalFeed()

    override suspend fun refreshData() {
        globalPage = 0
        val list = api.fetchReels(globalPage, pageSize)
        storage.saveGlobal(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getGlobalFeed().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMoreGlobal() {
        globalPage += 1
        val list = api.fetchReels(globalPage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendGlobal(list)
        } else {
            globalPage = (globalPage - 1).coerceAtLeast(0)
        }
    }

    // My Profile Feed
    override fun getMyProfileFeed(): Flow<List<Reel>> = storage.getMyProfileFeed()

    override suspend fun refreshMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage = 0
        val list = api.fetchUserReels(myId, myProfilePage, pageSize)
        storage.saveMyProfile(list)
    }

    override suspend fun loadMoreMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage += 1
        val list = api.fetchUserReels(myId, myProfilePage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendMyProfile(list)
        } else {
            myProfilePage = (myProfilePage - 1).coerceAtLeast(0)
        }
    }

    override fun getLikedReels(): Flow<Set<String>> = storage.getLikedReels()

    override suspend fun toggleReelLike(reelId: String) {
        storage.toggleReelLike(reelId)
    }

    override suspend fun shareReel(reelId: String) {
        storage.shareReel(reelId)
    }

    override suspend fun fetchUserReels(userId: String, page: Int, pageSize: Int): List<Reel> {
        return api.fetchUserReels(userId, page, pageSize)
    }
}
