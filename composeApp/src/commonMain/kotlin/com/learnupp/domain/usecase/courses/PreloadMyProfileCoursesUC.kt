package com.learnupp.domain.usecase.courses

import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessSuspendUseCase

abstract class PreloadMyProfileCoursesUseCase : ParameterlessSuspendUseCase<Unit>()

class PreloadMyProfileCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : PreloadMyProfileCoursesUseCase() {
    override suspend fun invoke() {
        repo.refreshMyProfile()
    }
}
