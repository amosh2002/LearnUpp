package com.learnupp.data.camera

import com.learnupp.domain.model.Camera
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CameraStorage {
    // Save methods
    suspend fun saveAllCameras(newObjects: List<Camera>)
    suspend fun saveFeaturedCamera(camera: Camera)

    // Get methods
    fun getAllCameras(): Flow<List<Camera>>
    fun getFeaturedCamera(): Flow<Camera?>

    // Clear all stored data
    fun clearAllData()
}

class InMemoryCameraStorage : CameraStorage {
    // Values stored in memory
    private val storedAllCameras = MutableStateFlow(emptyList<Camera>())
    private val storedFeaturedCamera = MutableStateFlow<Camera?>(null)

    // Save methods
    override suspend fun saveAllCameras(newObjects: List<Camera>) {
        storedAllCameras.value = newObjects.toList()
    }

    override suspend fun saveFeaturedCamera(camera: Camera) {
        storedFeaturedCamera.value = camera
    }

    // Get methods
    override fun getAllCameras(): Flow<List<Camera>> = storedAllCameras
    override fun getFeaturedCamera(): Flow<Camera?> = storedFeaturedCamera

    // Clear all stored data
    override fun clearAllData() {
        storedAllCameras.value = emptyList()
        storedFeaturedCamera.value = null
    }
}
