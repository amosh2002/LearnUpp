package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class SupabaseCoursesApi(
    private val client: SupabaseClient,
) : CoursesApi {
    override suspend fun fetchCourses(page: Int, pageSize: Int): List<Course> {
        return try {
            val offset = (page * pageSize).toLong()
            val end = offset + pageSize - 1

            client.from("courses").select(
                // Fetch Course columns + join Profiles table
                columns = Columns.raw("*, profiles(*)")
            ) {
                range(offset, end)
                // Filter logic can go here (e.g., .eq("category", "Mobile Dev"))
                order("created_at", order = Order.DESCENDING)
            }.decodeList<Course>()

        } catch (t: Throwable) {
            emptyList()
        }
    }
}