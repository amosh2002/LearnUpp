package com.learnupp.domain.repo

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository : BaseRepository {
    fun getCourses(): Flow<List<Course>>
    suspend fun loadMore()
}

