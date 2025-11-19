package com.learnupp.ui.videos

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.Video
import com.learnupp.domain.usecase.videos.GetLikedVideosUseCase
import com.learnupp.domain.usecase.videos.GetVideosUseCase
import com.learnupp.domain.usecase.videos.LoadMoreVideosUseCase
import com.learnupp.domain.usecase.videos.PreloadVideosUseCase
import com.learnupp.domain.usecase.videos.ReloadVideosUseCase
import com.learnupp.domain.usecase.videos.ShareVideoUseCase
import com.learnupp.domain.usecase.videos.ToggleVideoLikeUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VideosScreenModel(
    private val preloadVideos: PreloadVideosUseCase,
    private val reloadVideos: ReloadVideosUseCase,
    private val getVideos: GetVideosUseCase,
    private val getLiked: GetLikedVideosUseCase,
    private val toggleVideoLike: ToggleVideoLikeUseCase,
    private val loadMore: LoadMoreVideosUseCase,
    private val shareVideo: ShareVideoUseCase,
) : BaseScreenModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos = _videos.asStateFlow()
    val liked = getLiked().stateIn(screenModelScope, SharingStarted.Eagerly, emptySet())

    init {
        screenModelScope.launch {
            preloadVideos()
            loadVideos()
        }
    }

    private suspend fun loadVideos() {
        val list = getVideos()
            .catch { emit(emptyList()) }
            .flowOn(Dispatchers.IO)
            .first()
        _videos.value = list
    }

    suspend fun refresh() {
        reloadVideos()
        loadVideos()
    }

    fun loadMoreIfNeeded() {
        screenModelScope.launch {
            loadMore()
            loadVideos()
        }
    }

    fun toggleLike(videoId: String) {
        screenModelScope.launch {
            toggleVideoLike(videoId)
        }
    }

    fun registerShare(videoId: String) {
        screenModelScope.launch {
            shareVideo(videoId)
        }
    }
}



