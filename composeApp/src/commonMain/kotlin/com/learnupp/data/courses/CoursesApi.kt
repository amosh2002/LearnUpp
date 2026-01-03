package com.learnupp.data.courses

import com.learnupp.domain.model.Course

interface CoursesApi {
    suspend fun fetchCourses(page: Int, pageSize: Int): List<Course>
    suspend fun fetchUserCourses(userId: String, page: Int, pageSize: Int): List<Course>
}

