package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadCoursesUseCaseImpl(
    private val repository: CoursesRepository
) : PreloadCoursesUseCase() {
    override suspend fun invoke() {
        repository.preloadData()
    }
}

