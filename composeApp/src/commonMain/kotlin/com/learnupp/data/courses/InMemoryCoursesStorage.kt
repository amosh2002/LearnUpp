package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryCoursesStorage : CoursesStorage {
    private val coursesState = MutableStateFlow<List<Course>>(emptyList())

    override fun getCourses(): Flow<List<Course>> = coursesState.asStateFlow()

    override suspend fun save(courses: List<Course>) {
        coursesState.value = courses
    }

    override suspend fun append(courses: List<Course>) {
        coursesState.value = coursesState.value + courses
    }

    override suspend fun clear() {
        coursesState.value = emptyList()
    }
}

