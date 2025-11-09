package com.learnupp.domain.usecase.camera

import com.learnupp.domain.model.Camera
import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetFeaturedCameraUseCase : ParameterlessUseCase<Flow<Camera?>>()

class GetFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : GetFeaturedCameraUseCase() {
    override fun invoke(): Flow<Camera?> {
        return cameraRepository.getFeaturedCamera()
    }
}
