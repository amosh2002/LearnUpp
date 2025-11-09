package com.learnupp.domain.usecase.camera

import com.learnupp.domain.model.Camera
import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetAllCamerasUseCase : ParameterlessUseCase<Flow<List<Camera>>>()

class GetCarwashesUseCaseImpl(
    private val cameraRepository: CameraRepository
) : GetAllCamerasUseCase() {
    override fun invoke(): Flow<List<Camera>> {
        return cameraRepository.getAllCameras()
    }
}
