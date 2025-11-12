package com.learnupp.domain.repo

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideosRepository : BaseRepository {
    fun getVideos(): Flow<List<Video>>
    fun getLikedVideos(): Flow<Set<String>>
    suspend fun loadMore()
    suspend fun toggleVideoLike(videoId: String)
    suspend fun shareVideo(videoId: String)
}


