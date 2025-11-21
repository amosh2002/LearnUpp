package com.learnupp.data.courses

import com.learnupp.domain.repo.CoursesRepository
import kotlinx.coroutines.flow.firstOrNull

class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val storage: CoursesStorage
) : CoursesRepository {
    private var loadedPage = -1
    private val pageSize = 6

    override fun getCourses() = storage.getCourses()

    override suspend fun refreshData() {
        loadedPage = 0
        val list = api.fetchCourses(loadedPage, pageSize)
        storage.save(list)
    }

    override suspend fun needsRefresh(): Boolean {
        return storage.getCourses().firstOrNull().isNullOrEmpty()
    }

    override suspend fun loadMore() {
        loadedPage += 1
        if (loadedPage == 0) return
        val list = api.fetchCourses(loadedPage, pageSize)
        if (list.isNotEmpty()) {
            storage.append(list)
        }
    }
}

