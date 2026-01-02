package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import com.learnupp.util.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseCoursesApi(
    private val client: SupabaseClient,
) : CoursesApi {
    override suspend fun fetchCourses(page: Int, pageSize: Int): List<Course> {
        return try {
            val offset = (page * pageSize).toLong()
            val end = offset + pageSize - 1
            Logger.d("CoursesApi", "Supabase courses page=$page pageSize=$pageSize")

            val courses = client.from("courses")
                .select {
                    range(offset, end)
                }
                .decodeList<Course>()

            Logger.d("CoursesApi", "Supabase courses success count=${courses.size}")
            courses
        } catch (t: Throwable) {
            Logger.e("CoursesApi", "Supabase courses failed: ${t.message}")
            throw t
        }
    }
}