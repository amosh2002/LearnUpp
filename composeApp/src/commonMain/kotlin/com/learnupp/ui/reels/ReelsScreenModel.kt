package com.learnupp.ui.reels

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.Reel
import com.learnupp.domain.usecase.reels.GetLikedReelsUseCase
import com.learnupp.domain.usecase.reels.GetReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadReelsUseCase
import com.learnupp.domain.usecase.reels.ReloadReelsUseCase
import com.learnupp.domain.usecase.reels.ShareReelUseCase
import com.learnupp.domain.usecase.reels.ToggleReelLikeUseCase
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

class ReelsScreenModel(
    private val preloadReelsUseCase: PreloadReelsUseCase,
    private val reloadReelsUseCase: ReloadReelsUseCase,
    private val getReelsUseCase: GetReelsUseCase,
    private val getLikedReelsUseCase: GetLikedReelsUseCase,
    private val toggleReelLikeUseCase: ToggleReelLikeUseCase,
    private val loadMoreReelsUseCase: LoadMoreReelsUseCase,
    private val shareReelUseCase: ShareReelUseCase,
) : BaseScreenModel() {

    private val _reels = MutableStateFlow<List<Reel>>(emptyList())
    val reels = _reels.asStateFlow()
    val liked = getLikedReelsUseCase()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptySet())

    init {
        screenModelScope.launch {
            // Preload then load
            preloadReelsUseCase()
            loadReels()
        }
    }

    private suspend fun loadReels() {
        val list = getReelsUseCase()
            .catch { emit(emptyList()) }
            .flowOn(Dispatchers.IO)
            .first()
        _reels.value = list
    }

    fun refresh() {
        screenModelScope.launch {
            reloadReelsUseCase()
            loadReels()
        }
    }

    fun toggleReelLike(id: String) {
        screenModelScope.launch {
            toggleReelLikeUseCase(id)
        }
    }

    fun loadMore() {
        screenModelScope.launch {
            loadMoreReelsUseCase()
            loadReels()
        }
    }

    fun shareReel(id: String) {
        screenModelScope.launch {
            shareReelUseCase(id)
        }
    }
}


