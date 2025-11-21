package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesStorage {
    fun getCourses(): Flow<List<Course>>
    suspend fun save(courses: List<Course>)
    suspend fun append(courses: List<Course>)
    suspend fun clear()
}

