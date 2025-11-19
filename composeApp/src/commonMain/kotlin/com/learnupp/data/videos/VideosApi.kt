package com.learnupp.data.videos

import com.learnupp.domain.model.Video

interface VideosApi {
    suspend fun fetchVideos(page: Int, pageSize: Int): List<Video>
}



