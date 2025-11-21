package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class ReloadCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class ReloadCoursesUseCaseImpl(
    private val repository: CoursesRepository
) : ReloadCoursesUseCase() {
    override suspend fun invoke() {
        repository.refreshData()
    }
}

