package com.learnupp.ui.more

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.Profile
import com.learnupp.domain.usecase.auth.LogoutUseCase
import com.learnupp.domain.usecase.profile.UpdateProfileUseCase
import com.learnupp.domain.usecase.courses.GetMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadMyProfileCoursesUseCase
import com.learnupp.domain.usecase.courses.RefreshMyProfileCoursesUseCase
import com.learnupp.domain.usecase.profile.GetProfileUseCase
import com.learnupp.domain.usecase.profile.PreloadProfileUseCase
import com.learnupp.domain.usecase.profile.ReloadProfileUseCase
import com.learnupp.domain.usecase.reels.GetMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.LoadMoreMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.PreloadMyProfileReelsUseCase
import com.learnupp.domain.usecase.reels.RefreshMyProfileReelsUseCase
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
    private val updateProfile: UpdateProfileUseCase,
    // Videos
    private val preloadVideos: PreloadMyProfileVideosUseCase,
    private val refreshMyProfileVideos: RefreshMyProfileVideosUseCase,
    private val getMyProfileVideos: GetMyProfileVideosUseCase,
    private val loadMoreMyProfileVideos: LoadMoreMyProfileVideosUseCase,
    // Reels
    private val preloadReels: PreloadMyProfileReelsUseCase,
    private val refreshMyProfileReels: RefreshMyProfileReelsUseCase,
    private val getReels: GetMyProfileReelsUseCase,
    private val loadMoreReels: LoadMoreMyProfileReelsUseCase,
    // Courses
    private val preloadCourses: PreloadMyProfileCoursesUseCase,
    private val refreshMyProfileCourses: RefreshMyProfileCoursesUseCase,
    private val getCourses: GetMyProfileCoursesUseCase,
    private val loadMoreCourses: LoadMoreMyProfileCoursesUseCase,
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

    val courses = getCourses()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch {
            preloadProfile()
            preloadVideos()
            preloadReels()
            preloadCourses()
        }
    }

    private suspend fun reloadAll() {
        reloadProfile()
        refreshMyProfileVideos()
        refreshMyProfileReels()
        refreshMyProfileCourses()
    }

    fun refreshProfile() {
        screenModelScope.launch {
            reloadProfile()
            refreshMyProfileVideos()
            refreshMyProfileReels()
            refreshMyProfileCourses()
        }
    }

    fun loadMoreForVideos() {
        screenModelScope.launch { loadMoreMyProfileVideos() }
    }

    fun loadMoreForReels() {
        screenModelScope.launch { loadMoreReels() }
    }

    fun loadMoreForCourses() {
        screenModelScope.launch { loadMoreCourses() }
    }

    fun updateProfileInfo(
        username: String? = null,
        fullName: String? = null,
        about: String? = null
    ) {
        screenModelScope.launch {
            updateProfile(username, fullName, about)
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


