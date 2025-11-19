package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideosStorage {
    fun getVideos(): Flow<List<Video>>
    fun getLiked(): Flow<Set<String>>
    suspend fun save(videos: List<Video>)
    suspend fun append(videos: List<Video>)
    suspend fun toggleLike(videoId: String)
    suspend fun share(videoId: String)
}



