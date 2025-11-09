package com.learnupp.domain.usecase.camera

import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadFeaturedCameraUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadFeaturedCameraUseCaseImpl(
    private val cameraRepository: CameraRepository
) : ReloadFeaturedCameraUseCase() {
    override suspend fun invoke() {
        cameraRepository.refreshFeaturedCamera()
    }
}
