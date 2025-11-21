package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreCoursesUseCaseImpl(
    private val repository: CoursesRepository
) : LoadMoreCoursesUseCase() {
    override suspend fun invoke() {
        repository.loadMore()
    }
}

