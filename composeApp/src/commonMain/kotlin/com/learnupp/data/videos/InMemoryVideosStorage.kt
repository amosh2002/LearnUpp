package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryVideosStorage : VideosStorage {
    // Bucket 1: The Home Screen
    private val globalFeedState = MutableStateFlow<List<Video>>(emptyList())

    // Bucket 2: The "More" Screen (My Profile)
    private val myProfileState = MutableStateFlow<List<Video>>(emptyList())

    // Shared State: Likes (Applies to a video regardless of where it is shown)
    private val likedState = MutableStateFlow<Set<String>>(emptySet())

    // Getters based on context
    override fun getGlobalFeed(): Flow<List<Video>> = globalFeedState.asStateFlow()
    override fun getMyProfileFeed(): Flow<List<Video>> = myProfileState.asStateFlow()
    override fun getLiked(): Flow<Set<String>> = likedState.asStateFlow()

    override fun saveGlobal(videos: List<Video>) {
        globalFeedState.value = videos
    }

    override fun appendGlobal(videos: List<Video>) {
        globalFeedState.value += videos
    }

    override fun saveMyProfile(videos: List<Video>) {
        myProfileState.value = videos
    }

    override fun appendMyProfile(videos: List<Video>) {
        myProfileState.value += videos
    }

    // Toggling a like should ideally update the UI in BOTH lists if the video exists in both
    override suspend fun toggleLike(videoId: String) {
        val currentLikes = likedState.value
        likedState.value =
            if (currentLikes.contains(videoId)) currentLikes - videoId else currentLikes + videoId

        // Optional: Update the actual video objects isLiked property in the lists if you store that locally
    }
}



