package com.learnupp.domain.usecase.camera

import com.learnupp.domain.repo.CameraRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadAllCamerasUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadAllCamerasUseCaseImpl(
    private val cameraRepository: CameraRepository
) : ReloadAllCamerasUseCase() {
    override suspend fun invoke() {
        cameraRepository.refreshData()
    }
}
