package com.learnupp.domain.usecase.camera

import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadAllCamerasUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadAllCamerasUseCaseImpl(
    private val cameraRepository: CameraRepository
) : PreloadAllCamerasUseCase() {
    override suspend fun invoke() {
        cameraRepository.preloadData()
    }
}
