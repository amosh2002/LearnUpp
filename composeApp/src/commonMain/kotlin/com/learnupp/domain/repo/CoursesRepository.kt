package com.learnupp.domain.repo

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository : BaseRepository {
    // Global Feed
    fun getGlobalFeed(): Flow<List<Course>>
    suspend fun loadMoreGlobal()

    // My Profile Feed
    fun getMyProfileFeed(): Flow<List<Course>>
    suspend fun refreshMyProfile()
    suspend fun loadMoreMyProfile()

    // Other Users (Stateless)
    suspend fun fetchUserCourses(userId: String, page: Int, pageSize: Int): List<Course>
}

