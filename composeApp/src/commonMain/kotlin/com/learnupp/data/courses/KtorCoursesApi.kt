package com.learnupp.data.courses

import com.learnupp.data.apiBaseUrl
import com.learnupp.domain.model.Course
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorCoursesApi(
    private val client: HttpClient,
) : CoursesApi {
    override suspend fun fetchCourses(page: Int, pageSize: Int): List<Course> =
        client.get("$apiBaseUrl/courses") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
}


