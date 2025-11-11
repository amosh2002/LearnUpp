package com.learnupp.data.reels

import com.learnupp.domain.model.Reel
import kotlin.random.Random

/**
 * Mock implementation returning paged sets of learning reels.
 * Image URLs intentionally use generic CDN images that are stable for demos.
 */
class MockReelsApi : ReelsApi {
    private val thumbs = listOf(
        "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?q=80&w=1200&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1517433456452-f9633a875f6f?q=80&w=1200&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1532614338840-ab30cf10ed36?q=80&w=1200&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1529101091764-c3526daf38fe?q=80&w=1200&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1518779578993-ec3579fee39f?q=80&w=1200&auto=format&fit=crop"
    )

    override suspend fun fetchReels(page: Int, pageSize: Int): List<Reel> {
        val start = page * pageSize
        return (start until start + pageSize).map { index ->
            val id = "reel-$index"
            val random = Random(index)
            // Randomly decide if this reel has a full course (about 60% chance)
            val hasFullCourse = random.nextFloat() < 0.6f
            val fullCourseId = if (hasFullCourse) {
                "course-${index + 100}" // Generate a course ID
            } else {
                null
            }

            // Add description only when there's no course (about 70% chance for reels without course)
            val description =
                when (index % 5) {
                    0 -> "Learn the fundamentals of design thinking and create beautiful interfaces"
                    1 -> "Master async programming with Kotlin's powerful coroutines API"
                    2 -> "Explore modern Swift concurrency patterns and best practices"
                    3 -> "Build scalable architectures using clean code principles"
                    else -> "Tips and tricks for cross-platform development with Compose"
                }

            Reel(
                id = id,
                title = when (index % 5) {
                    0 -> "5 Essential Design Principles"
                    1 -> "Kotlin Coroutines in 5 Minutes"
                    2 -> "Swift Concurrency Basics"
                    3 -> "Architecting Clean Use Cases"
                    else -> "Compose Multiplatform Tips"
                },
                // Note: Video URLs are placeholders. We'll render using platform players.
                videoUrl = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4",
                thumbnailUrl = thumbs[index % thumbs.size],
                authorName = listOf(
                    "Sarah Johnson",
                    "Alex Kim",
                    "Emily Chen",
                    "Michael Brown",
                    "Ava Lee"
                )[index % 5],
                authorTitle = listOf(
                    "UI/UX Designer",
                    "Android Engineer",
                    "iOS Developer",
                    "Software Architect",
                    "Mobile Dev"
                )[index % 5],
                likesCount = 800 + random.nextInt(900),
                commentsCount = 20 + random.nextInt(120),
                fullCourseId = fullCourseId,
                description = description
            )
        }
    }
}


