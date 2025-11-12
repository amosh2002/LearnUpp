package com.learnupp.data.videos

import com.learnupp.domain.repo.VideosRepository
import kotlinx.coroutines.flow.firstOrNull

class VideosRepositoryImpl(
    private val storage: VideosStorage,
    private val api: VideosApi
) : VideosRepository {
    private var loadedPage = -1
    private val pageSize = 8

    override fun getVideos() = storage.getVideos()
    override fun getLikedVideos() = storage.getLiked()

    override suspend fun refreshData() {
        loadedPage = 0
        val list = api.fetchVideos(loadedPage, pageSize)
        storage.save(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getVideos().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMore() {
        loadedPage += 1
        if (loadedPage == 0) return
        val list = api.fetchVideos(loadedPage, pageSize)
        storage.append(list)
    }

    override suspend fun toggleVideoLike(videoId: String) {
        storage.toggleLike(videoId)
    }

    override suspend fun shareVideo(videoId: String) {
        storage.share(videoId)
    }
}


