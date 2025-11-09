package com.learnupp.domain.usecase.camera

import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadFeaturedCameraUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : PreloadFeaturedCameraUseCase() {
    override suspend fun invoke() {
        cameraRepository.preloadFeaturedCamera()
    }
}
