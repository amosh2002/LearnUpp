package com.learnupp.domain.usecase.courses

import com.learnupp.domain.model.Course
import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetGlobalCoursesUseCase : ParameterlessUseCase<Flow<List<Course>>>()

class GetGlobalCoursesUseCaseImpl(
    private val repo: CoursesRepository
) : GetGlobalCoursesUseCase() {
    override fun invoke(): Flow<List<Course>> = repo.getGlobalFeed()
}
