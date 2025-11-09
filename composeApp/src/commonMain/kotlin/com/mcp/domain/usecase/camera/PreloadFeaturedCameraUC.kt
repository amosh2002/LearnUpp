package com.mcp.domain.usecase.camera

import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadFeaturedCameraUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : PreloadFeaturedCameraUseCase() {
    override suspend fun invoke() {
        cameraRepository.preloadFeaturedCamera()
    }
}
