package com.mcp.data.camera

import com.mcp.domain.repo.CameraRepository
import kotlinx.coroutines.flow.firstOrNull

class CameraRepositoryImpl(
    private val cameraStorage: CameraStorage,
    private val cameraApi: CameraApi,
) : CameraRepository {

    // All Cameras
    override fun getAllCameras() = cameraStorage.getAllCameras()

    override suspend fun refreshData() {
        val cameras = cameraApi.fetchAllCameras()
        cameraStorage.saveAllCameras(cameras)
    }

    override suspend fun needsRefresh(): Boolean {
        return cameraStorage.getAllCameras().firstOrNull().isNullOrEmpty()
    }

    // Featured Camera
    override fun getFeaturedCamera() = cameraStorage.getFeaturedCamera()

    override suspend fun refreshFeaturedCamera() {
        val camera = cameraApi.fetchFeaturedCamera()
        cameraStorage.saveFeaturedCamera(camera)
    }

    override suspend fun preloadFeaturedCamera() {
        if (cameraStorage.getFeaturedCamera().firstOrNull() == null) {
            refreshFeaturedCamera()
        }
    }
}