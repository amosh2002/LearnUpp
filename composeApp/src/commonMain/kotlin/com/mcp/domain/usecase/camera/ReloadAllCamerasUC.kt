package com.mcp.domain.usecase.camera

import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadAllCamerasUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadAllCamerasUseCaseImpl(
    private val cameraRepository: CameraRepository
) : ReloadAllCamerasUseCase() {
    override suspend fun invoke() {
        cameraRepository.refreshData()
    }
}
