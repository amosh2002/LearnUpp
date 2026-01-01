package com.learnupp.data.courses

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Course
import com.learnupp.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorCoursesApi(
    private val client: HttpClient,
) : CoursesApi {
    override suspend fun fetchCourses(page: Int, pageSize: Int): List<Course> {
        Logger.d("CoursesApi", "GET /courses page=$page pageSize=$pageSize")
        return try {
            val courses = client.get("$apiBaseUrl/courses") {
                parameter("page", page)
                parameter("pageSize", pageSize)
            }.body<List<Course>>()
            Logger.d("CoursesApi", "GET /courses success count=${courses.size}")
            courses
        } catch (t: Throwable) {
            Logger.e("CoursesApi", "GET /courses failed: ${t.message}")
            throw t
        }
    }
}


