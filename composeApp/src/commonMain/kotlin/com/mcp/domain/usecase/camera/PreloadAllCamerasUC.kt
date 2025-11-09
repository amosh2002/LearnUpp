package com.mcp.domain.usecase.camera

import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadAllCamerasUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadAllCamerasUseCaseImpl(
    private val cameraRepository: CameraRepository
) : PreloadAllCamerasUseCase() {
    override suspend fun invoke() {
        cameraRepository.preloadData()
    }
}
