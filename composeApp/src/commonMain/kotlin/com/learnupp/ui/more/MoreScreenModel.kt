package com.learnupp.ui.more

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.Profile
import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.domain.usecase.profile.GetProfileUseCase
import com.learnupp.domain.usecase.profile.PreloadProfileUseCase
import com.learnupp.domain.usecase.profile.ReloadProfileUseCase
import com.learnupp.domain.usecase.profile.UpdateProfileAboutUseCase
import com.learnupp.domain.usecase.reels.GetReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadReelsUseCase
import com.learnupp.domain.usecase.reels.ReloadReelsUseCase
import com.learnupp.domain.usecase.videos.GetMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.LoadMoreMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.PreloadMyProfileVideosUseCase
import com.learnupp.domain.usecase.videos.RefreshMyProfileVideosUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ScreenModel for the profile (More) screen.
 * Uses mocked repositories wired via DI so the feature is API-ready.
 */
class MoreScreenModel(
    private val logoutUseCase: LogoutUseCase,
    // Profile
    private val preloadProfile: PreloadProfileUseCase,
    private val reloadProfile: ReloadProfileUseCase,
    private val getProfile: GetProfileUseCase,
    private val updateProfileAbout: UpdateProfileAboutUseCase,
    // Videos
    private val preloadVideos: PreloadMyProfileVideosUseCase,
    private val refreshMyProfileVideos: RefreshMyProfileVideosUseCase,
    private val getMyProfileVideos: GetMyProfileVideosUseCase,
    private val loadMoreMyProfileVideos: LoadMoreMyProfileVideosUseCase,
    // Reels
    private val preloadReels: PreloadReelsUseCase,
    private val reloadReels: ReloadReelsUseCase,
    private val getReels: GetReelsUseCase,
    private val loadMoreReels: LoadMoreReelsUseCase,
) : BaseScreenModel() {

    // Loading used for destructive actions like logout
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val profile = getProfile()
        .stateIn(screenModelScope, SharingStarted.Eagerly, Profile.empty())

    val videos = getMyProfileVideos()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    val reels = getReels()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch {
            preloadProfile()
            preloadVideos()
            preloadReels()
            reloadAll()
        }
    }

    private suspend fun reloadAll() {
        reloadProfile()
        refreshMyProfileVideos()
        reloadReels()
    }

    fun refreshProfile() {
        screenModelScope.launch { 
            reloadProfile()
            refreshMyProfileVideos()
        }
    }

    fun loadMoreForVideos() {
        screenModelScope.launch { loadMoreMyProfileVideos() }
    }

    fun loadMoreForReels() {
        screenModelScope.launch { loadMoreReels() }
    }

    fun updateAbout(text: String) {
        screenModelScope.launch {
            updateProfileAbout(text)
            reloadProfile()
        }
    }

    suspend fun logout(): Boolean {
        _isLoading.value = true
        val success = logoutUseCase()
        _isLoading.value = false
        return success
    }
}


