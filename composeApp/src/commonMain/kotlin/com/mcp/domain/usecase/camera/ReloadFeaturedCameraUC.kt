package com.mcp.domain.usecase.camera

import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadFeaturedCameraUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : ReloadFeaturedCameraUseCase() {
    override suspend fun invoke() {
        cameraRepository.refreshFeaturedCamera()
    }
}
