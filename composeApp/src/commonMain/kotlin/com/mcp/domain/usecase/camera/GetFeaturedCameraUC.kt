package com.mcp.domain.usecase.camera

import com.mcp.domain.model.Camera
import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetFeaturedCameraUseCase : ParameterlessUseCase<Flow<Camera?>>()

class GetFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : GetFeaturedCameraUseCase() {
    override fun invoke(): Flow<Camera?> {
        return cameraRepository.getFeaturedCamera()
    }
}
