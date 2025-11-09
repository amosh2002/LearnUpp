package com.learnupp.domain.repo

import com.learnupp.domain.model.Camera
import kotlinx.coroutines.flow.Flow

interface CameraRepository : BaseRepository {
    // Get cameras from local storage
    fun getAllCameras(): Flow<List<Camera>>

    // Preload featured camera from API and update local storage.
    // Checks if already loaded to avoid redundant calls
    suspend fun preloadFeaturedCamera()

    // Refresh featured camera from API and update local storage
    suspend fun refreshFeaturedCamera()

    // Get the featured camera from local storage
    fun getFeaturedCamera(): Flow<Camera?>
}