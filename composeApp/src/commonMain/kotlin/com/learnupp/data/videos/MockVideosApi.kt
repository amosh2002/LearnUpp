package com.learnupp.data.videos

import com.learnupp.domain.model.Video
import kotlin.random.Random

class MockVideosApi : VideosApi {
    private val thumbs = listOf(
        "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?q=80&w=1600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?q=80&w=1600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?q=80&w=1600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1520975916090-3105956dac38?q=80&w=1600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1485846234645-a62644f84728?q=80&w=1600&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1469474968028-56623f02e42e?q=80&w=1600&auto=format&fit=crop"
    )

    override suspend fun fetchVideos(page: Int, pageSize: Int): List<Video> {
        val start = page * pageSize
        return (start until start + pageSize).map { index ->
            val r = Random(index)
            val duration = 60 + r.nextInt(8 * 60)
            val id = "video-$index"
            val author = listOf("Wanderlust Diaries", "Code Academy", "Design Hub", "Swift Lab")[index % 4]
            val title = listOf(
                "Exploring the Red Canyons: Adventure of a Lifetime",
                "Mastering Kotlin Coroutines: From Zero to Hero",
                "Designing Effective UI: Principles that Matter",
                "Swift Concurrency Deep Dive: Structured Concurrency"
            )[index % 4]

            Video(
                id = id,
                title = title,
                previewImageUrl = thumbs[index % thumbs.size],
                durationSec = duration,
                authorName = author,
                channelName = author,
                viewsCount = 80_000 + r.nextInt(60_000),
                likesCount = 10 + r.nextInt(200),
                fullCourseId = if (r.nextFloat() < 0.4f) "course-${index + 200}" else null,
                fullVideoUrl = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4"
            )
        }
    }
}



