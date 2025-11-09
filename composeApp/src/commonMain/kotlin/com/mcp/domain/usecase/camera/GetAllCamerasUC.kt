package com.mcp.domain.usecase.camera

import com.mcp.domain.model.Camera
import com.mcp.domain.repo.CameraRepository
import com.mcp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetAllCamerasUseCase : ParameterlessUseCase<Flow<List<Camera>>>()

class GetCarwashesUseCaseImpl(
    private val cameraRepository: CameraRepository
) : GetAllCamerasUseCase() {
    override fun invoke(): Flow<List<Camera>> {
        return cameraRepository.getAllCameras()
    }
}
