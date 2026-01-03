package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class LoadMoreMyProfileCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class LoadMoreMyProfileCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : LoadMoreMyProfileCoursesUseCase() {
    override suspend fun invoke() {
        repo.loadMoreMyProfile()
    }
}
