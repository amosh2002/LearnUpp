package com.learnupp.data.reels

import com.learnupp.domain.repo.ReelsRepository
import kotlinx.coroutines.flow.firstOrNull

class ReelsRepositoryImpl(
    private val storage: ReelsStorage,
    private val api: ReelsApi
) : ReelsRepository {
    private var loadedPage = -1
    private val pageSize = 6

    override fun getReels() = storage.getReels()
    override fun getLikedReels() = storage.getLikedReels()

    override suspend fun refreshData() {
        loadedPage = 0
        val list = api.fetchReels(loadedPage, pageSize)
        storage.saveReels(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getReels().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMore() {
        loadedPage += 1
        if (loadedPage == 0) {
            // already loaded in refresh
            return
        }
        val list = api.fetchReels(loadedPage, pageSize)
        storage.appendReels(list)
    }

    override suspend fun toggleReelLike(reelId: String) {
        storage.toggleReelLike(reelId)
    }

    override suspend fun shareReel(reelId: String) {
        storage.shareReel(reelId)
    }
}


