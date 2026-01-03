package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class RefreshMyProfileCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class RefreshMyProfileCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : RefreshMyProfileCoursesUseCase() {
    override suspend fun invoke() {
        repo.refreshMyProfile()
    }
}
