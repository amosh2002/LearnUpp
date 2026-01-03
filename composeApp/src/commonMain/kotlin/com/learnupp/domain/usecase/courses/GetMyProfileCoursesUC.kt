package com.learnupp.domain.usecase.courses

import com.learnupp.domain.model.Course
import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetMyProfileCoursesUseCase : ParameterlessUseCase<Flow<List<Course>>>()

class GetMyProfileCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : GetMyProfileCoursesUseCase() {
    override fun invoke(): Flow<List<Course>> = repo.getMyProfileFeed()
}
