package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.domain.repo.VideosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class VideosRepositoryImpl(
    private val storage: VideosStorage,
    private val api: VideosApi,
    private val authRepository: AuthRepository
) : VideosRepository {

    private val pageSize = 8

    // Track pagination separately for different contexts
    private var globalPage = -1
    private var myProfilePage = -1

    // --- Global Feed Logic ---
    override fun getGlobalFeed(): Flow<List<Video>> = storage.getGlobalFeed()

    override suspend fun refreshData() {
        globalPage = 0
        val list = api.fetchVideos(globalPage, pageSize)
        storage.saveGlobal(list)
    }

    override suspend fun needsRefresh(): Boolean {
        // Only refresh if we have absolutely no data
        val currentData = storage.getGlobalFeed().firstOrNull()
        return currentData.isNullOrEmpty()
    }

    override suspend fun loadMoreGlobal() {
        globalPage += 1
        val list = api.fetchVideos(globalPage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendGlobal(list)
        } else {
            // Optional: Revert page increment if empty to prevent infinite empty fetching
            globalPage = (globalPage - 1).coerceAtLeast(0)
        }
    }

    // --- My Profile Logic ---
    override fun getMyProfileFeed(): Flow<List<Video>> = storage.getMyProfileFeed()

    override suspend fun refreshMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage = 0
        // Reusing the generic 'ByAuthor' API call
        val list = api.fetchVideosByAuthor(myId, myProfilePage, pageSize)
        storage.saveMyProfile(list)
    }

    override suspend fun preloadMyProfile() {
        if (myProfileNeedsRefresh()) {
            refreshMyProfile()
        }
    }

    override suspend fun myProfileNeedsRefresh(): Boolean {
        // Only refresh if we have absolutely no data
        // Could be enhanced with timestamps or other logic
        return storage.getMyProfileFeed().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMoreMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage += 1
        val list = api.fetchVideosByAuthor(myId, myProfilePage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendMyProfile(list)
        } else {
            myProfilePage = (myProfilePage - 1).coerceAtLeast(0)
        }
    }

    // --- Other User Logic (Stateless) ---
    override suspend fun fetchUserVideos(userId: String, page: Int): List<Video> {
        return api.fetchVideosByAuthor(userId, page, pageSize)
    }

    // --- Likes & Sharing ---
    override fun getLikedVideos(): Flow<Set<String>> {
        return storage.getLiked()
    }

    override suspend fun toggleVideoLike(videoId: String) {
        // 1. Update Local UI instantly (Optimistic update)
        storage.toggleLike(videoId)
        // 2. TODO: Fire & Forget network call to Supabase to persist the like
        // api.likeVideo(videoId)
    }

    override suspend fun shareVideo(videoId: String) {
        // Usually handled by UI, but we can track it here or analytics
//        storage.share(videoId)
    }
}