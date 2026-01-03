package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesStorage {
    fun getGlobalFeed(): Flow<List<Course>>
    fun getMyProfileFeed(): Flow<List<Course>>
    
    suspend fun saveGlobal(courses: List<Course>)
    suspend fun appendGlobal(courses: List<Course>)
    
    suspend fun saveMyProfile(courses: List<Course>)
    suspend fun appendMyProfile(courses: List<Course>)
    
    suspend fun clear()
}

