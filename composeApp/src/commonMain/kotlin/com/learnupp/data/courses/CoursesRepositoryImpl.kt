package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import com.learnupp.domain.repo.CoursesRepository
import kotlinx.coroutines.flow.firstOrNull

class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val storage: CoursesStorage,
    private val authRepository: com.learnupp.domain.repo.AuthRepository
) : CoursesRepository {
    private val pageSize = 6
    
    private var globalPage = -1
    private var myProfilePage = -1

    // Global
    override fun getGlobalFeed() = storage.getGlobalFeed()

    override suspend fun refreshData() {
        globalPage = 0
        val list = api.fetchCourses(globalPage, pageSize)
        storage.saveGlobal(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getGlobalFeed().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMoreGlobal() {
        globalPage += 1
        val list = api.fetchCourses(globalPage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendGlobal(list)
        } else {
            globalPage = (globalPage - 1).coerceAtLeast(0)
        }
    }

    // My Profile
    override fun getMyProfileFeed() = storage.getMyProfileFeed()

    override suspend fun refreshMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage = 0
        val list = api.fetchUserCourses(myId, myProfilePage, pageSize)
        storage.saveMyProfile(list)
    }

    override suspend fun loadMoreMyProfile() {
        val myId = authRepository.currentUserOrNull()?.id ?: return
        myProfilePage += 1
        val list = api.fetchUserCourses(myId, myProfilePage, pageSize)
        if (list.isNotEmpty()) {
            storage.appendMyProfile(list)
        } else {
            myProfilePage = (myProfilePage - 1).coerceAtLeast(0)
        }
    }

    override suspend fun fetchUserCourses(userId: String, page: Int, pageSize: Int): List<Course> {
        return api.fetchUserCourses(userId, page, pageSize)
    }
}

