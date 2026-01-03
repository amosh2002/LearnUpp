package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadGlobalCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadGlobalCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : PreloadGlobalCoursesUseCase() {
    override suspend fun invoke() {
        repo.preloadData()
    }
}
