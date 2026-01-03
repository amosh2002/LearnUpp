package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryCoursesStorage : CoursesStorage {
    private val globalCoursesState = MutableStateFlow<List<Course>>(emptyList())
    private val myProfileCoursesState = MutableStateFlow<List<Course>>(emptyList())

    override fun getGlobalFeed(): Flow<List<Course>> = globalCoursesState.asStateFlow()
    override fun getMyProfileFeed(): Flow<List<Course>> = myProfileCoursesState.asStateFlow()

    override suspend fun saveGlobal(courses: List<Course>) {
        globalCoursesState.value = courses
    }

    override suspend fun appendGlobal(courses: List<Course>) {
        globalCoursesState.value += courses
    }

    override suspend fun saveMyProfile(courses: List<Course>) {
        myProfileCoursesState.value = courses
    }

    override suspend fun appendMyProfile(courses: List<Course>) {
        myProfileCoursesState.value += courses
    }

    override suspend fun clear() {
        globalCoursesState.value = emptyList()
        myProfileCoursesState.value = emptyList()
    }
}

