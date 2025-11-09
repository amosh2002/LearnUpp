package com.mcp.ui.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.mcp.domain.model.Camera
import com.mcp.domain.usecase.camera.GetAllCamerasUseCase
import com.mcp.domain.usecase.camera.GetFeaturedCameraUseCase
import com.mcp.domain.usecase.camera.PreloadAllCamerasUseCase
import com.mcp.domain.usecase.camera.PreloadFeaturedCameraUseCase
import com.mcp.domain.usecase.camera.ReloadAllCamerasUseCase
import com.mcp.domain.usecase.camera.ReloadFeaturedCameraUseCase
import com.mcp.ui.base.BaseScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val preloadAllCamerasUseCase: PreloadAllCamerasUseCase,
    private val reloadAllCamerasUseCase: ReloadAllCamerasUseCase,
    private val getAllCamerasUseCase: GetAllCamerasUseCase,
    private val preloadFeaturedCameraUseCase: PreloadFeaturedCameraUseCase,
    private val reloadFeaturedCameraUseCase: ReloadFeaturedCameraUseCase,
    private val getFeaturedCameraUseCase: GetFeaturedCameraUseCase
) : BaseScreenModel() {

    init {
        screenModelScope.launch {
            // Preload data
            preloadAllCamerasUseCase()
            preloadFeaturedCameraUseCase()

            // Load data to state flows
            loadAllCameras()
            loadFeaturedCamera()
        }
    }

    // Loading State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // All Cameras
    private val _allCameras = MutableStateFlow<List<Camera>>(emptyList())
    val allCameras = _allCameras.asStateFlow()

    private suspend fun loadAllCameras() {
        // Use the promotedCarwashesUseCase
        val list = getAllCamerasUseCase()
            .catch { emit(emptyList()) }
            .flowOn(Dispatchers.IO)
            .first()
        _allCameras.value = list
    }

    fun refreshAllCameras() {
        screenModelScope.launch {
            reloadAllCamerasUseCase()
            loadAllCameras()
        }
    }

    // Featured Camera
    private val _featuredCamera = MutableStateFlow<Camera?>(null)
    val featuredCamera = _featuredCamera.asStateFlow()

    private suspend fun loadFeaturedCamera() {
        val camera = getFeaturedCameraUseCase()
            .catch { emit(null) }
            .flowOn(Dispatchers.IO)
            .first()
        _featuredCamera.value = camera
    }

    fun refreshFeaturedCamera() {
        screenModelScope.launch {
            reloadFeaturedCameraUseCase()
            loadFeaturedCamera()
        }
    }

}


