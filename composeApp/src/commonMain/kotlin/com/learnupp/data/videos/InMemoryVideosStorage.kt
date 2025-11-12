package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryVideosStorage : VideosStorage {
    private val videosState = MutableStateFlow<List<Video>>(emptyList())
    private val likedState = MutableStateFlow<Set<String>>(emptySet())

    override fun getVideos(): Flow<List<Video>> = videosState.asStateFlow()
    override fun getLiked(): Flow<Set<String>> = likedState.asStateFlow()

    override suspend fun save(videos: List<Video>) {
        videosState.value = videos
    }

    override suspend fun append(videos: List<Video>) {
        videosState.value += videos
    }

    override suspend fun toggleLike(videoId: String) {
        likedState.value = likedState.value.let { set ->
            if (set.contains(videoId)) set - videoId else set + videoId
        }
    }

    override suspend fun share(videoId: String) {
        // No-op for now
    }
}


