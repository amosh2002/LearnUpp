package com.learnupp.domain.repo

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideosRepository : BaseRepository {
    fun getGlobalFeed(): Flow<List<Video>>
    suspend fun loadMoreGlobal()
    fun getMyProfileFeed(): Flow<List<Video>>
    suspend fun loadMoreMyProfile()
    suspend fun refreshMyProfile()
    suspend fun preloadMyProfile()
    suspend fun myProfileNeedsRefresh(): Boolean
    suspend fun fetchUserVideos(userId: String, page: Int): List<Video>
    fun getLikedVideos(): Flow<Set<String>>
    suspend fun toggleVideoLike(videoId: String)
    suspend fun shareVideo(videoId: String)
}



