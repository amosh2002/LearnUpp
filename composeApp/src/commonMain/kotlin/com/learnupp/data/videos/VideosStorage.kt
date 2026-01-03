package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideosStorage {
    fun getGlobalFeed(): Flow<List<Video>>
    fun getMyProfileFeed(): Flow<List<Video>>
    fun getLiked(): Flow<Set<String>>
    fun saveGlobal(videos: List<Video>)
    fun appendGlobal(videos: List<Video>)
    fun saveMyProfile(videos: List<Video>)
    fun appendMyProfile(videos: List<Video>)
    suspend fun toggleLike(videoId: String)
}



