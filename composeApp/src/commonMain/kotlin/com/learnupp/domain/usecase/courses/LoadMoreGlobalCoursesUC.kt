package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreGlobalCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreGlobalCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : LoadMoreGlobalCoursesUseCase() {
    override suspend fun invoke() {
        repo.loadMoreGlobal()
    }
}
