package com.learnupp.data.courses

import com.learnupp.domain.model.Course
import kotlin.math.roundToInt
import kotlin.random.Random

class MockCoursesApi : CoursesApi {
    private val titles = listOf(
        "The Complete Guide to Your Ready Presentation",
        "Mastering Modern UI Storytelling",
        "Advanced UX Research For Creators",
        "AI Productivity For Course Teams"
    )
    private val instructors = listOf(
        "Sarah Johnson",
        "Michael Alvarez",
        "Emily Chen",
        "Liam Walker"
    )
    private val categories = listOf("Fitness", "Design", "Marketing", "Product")
    private val durations = listOf("4 days 2 hrs", "8 hrs", "12 hrs", "3 days 6 hrs")
    private val thumbs = listOf(
        "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?q=80&w=1900&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=1900&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?q=80&w=1900&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=1900&auto=format&fit=crop"
    )

    private val total = 40

    override suspend fun fetchCourses(page: Int, pageSize: Int): List<Course> {
        val start = page * pageSize
        if (start >= total) return emptyList()
        val end = minOf(start + pageSize, total)
        return (start until end).map { index ->
            val random = Random(index)
            val hasDiscount = index % 2 == 0
            val currentPrice = 29.0 + random.nextDouble(10.0, 40.0)
            val originalPrice = if (hasDiscount) currentPrice * 1.8 else null
            Course(
                id = "course-$index",
                title = titles[index % titles.size],
                instructorName = instructors[index % instructors.size],
                previewImageUrl = thumbs[index % thumbs.size],
                category = categories[index % categories.size],
                rating = (38 + random.nextInt(20)) / 10.0,
                ratingCount = 60 + random.nextInt(400),
                lecturesCount = 30 + random.nextInt(80),
                durationText = durations[index % durations.size],
                currentPrice = (currentPrice * 100).roundToInt() / 100.0,
                originalPrice = originalPrice?.let { (it * 100).roundToInt() / 100.0 },
                discountPercent = originalPrice?.let {
                    (((it - currentPrice) / it) * 100).roundToInt()
                }
            )
        }
    }
}

