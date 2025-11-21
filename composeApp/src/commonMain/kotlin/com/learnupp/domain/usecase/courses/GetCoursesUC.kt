package com.learnupp.domain.usecase.courses

import com.learnupp.domain.model.Course
import com.learnupp.domain.repo.CoursesRepository
import com.learnupp.domain.usecase.base.ParameterlessUseCase
import kotlinx.coroutines.flow.Flow

abstract class GetCoursesUseCase : ParameterlessUseCase<Flow<List<Course>>>()

class GetCoursesUseCaseImpl(
    private val repository: CoursesRepository
) : GetCoursesUseCase() {
    override fun invoke(): Flow<List<Course>> = repository.getCourses()
}

