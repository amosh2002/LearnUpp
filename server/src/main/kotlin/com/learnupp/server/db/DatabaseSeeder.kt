package com.learnupp.server.db

import com.learnupp.util.Logger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.roundToInt
import kotlin.random.Random

object DatabaseSeeder {
    private var db: Database? = null

    fun initialize(database: Database) {
        db = database
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(
                VideosTable,
                ReelsTable,
                CoursesTable,
                ProfilesTable
            )
        }
    }

    fun seedIfEmpty() {
        val database = db ?: return
        transaction(database) {
            if (VideosTable.selectAll().empty()) seedVideos()
            if (ReelsTable.selectAll().empty()) seedReels()
            if (CoursesTable.selectAll().empty()) seedCourses()
            if (ProfilesTable.selectAll().empty()) seedProfiles()
        }
    }

    private fun seedVideos() {
        val thumbs = listOf(
            "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?q=80&w=1600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1519681393784-d120267933ba?q=80&w=1600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?q=80&w=1600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1520975916090-3105956dac38?q=80&w=1600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1485846234645-a62644f84728?q=80&w=1600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1469474968028-56623f02e42e?q=80&w=1600&auto=format&fit=crop"
        )
        val authors = listOf("Wanderlust Diaries", "Code Academy", "Design Hub", "Swift Lab")
        val titles = listOf(
            "Exploring the Red Canyons: Adventure of a Lifetime",
            "Mastering Kotlin Coroutines: From Zero to Hero",
            "Designing Effective UI: Principles that Matter",
            "Swift Concurrency Deep Dive: Structured Concurrency"
        )

        val total = 50
        (0 until total).forEach { index ->
            val r = Random(index)
            val duration = 60 + r.nextInt(8 * 60)
            val id = "video-$index"
            val author = authors[index % authors.size]
            val title = titles[index % titles.size]

            VideosTable.insert {
                it[VideosTable.id] = id
                it[VideosTable.title] = title
                it[previewImageUrl] = thumbs[index % thumbs.size]
                it[durationSec] = duration
                it[authorName] = author
                it[channelName] = author
                it[viewsCount] = 80_000 + r.nextInt(60_000)
                it[likesCount] = 10 + r.nextInt(200)
                it[fullCourseId] = if (r.nextFloat() < 0.4f) "course-${index + 200}" else null
                it[fullVideoUrl] = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4"
            }
        }
        Logger.i("Seeder", "Seeded $total videos")
    }

    private fun seedReels() {
        val thumbs = listOf(
            "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?q=80&w=1200&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1517433456452-f9633a875f6f?q=80&w=1200&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1532614338840-ab30cf10ed36?q=80&w=1200&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1529101091764-c3526daf38fe?q=80&w=1200&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1518779578993-ec3579fee39f?q=80&w=1200&auto=format&fit=crop"
        )
        val titles = listOf(
            "5 Essential Design Principles",
            "Kotlin Coroutines in 5 Minutes",
            "Swift Concurrency Basics",
            "Architecting Clean Use Cases",
            "Compose Multiplatform Tips"
        )
        val authors = listOf(
            "Sarah Johnson" to "UI/UX Designer",
            "Alex Kim" to "Android Engineer",
            "Emily Chen" to "iOS Developer",
            "Michael Brown" to "Software Architect",
            "Ava Lee" to "Mobile Dev"
        )

        val pageSize = 10
        (0 until 40).forEach { index ->
            val random = Random(index)
            val (name, title) = authors[index % authors.size]
            val hasFullCourse = random.nextFloat() < 0.6f
            val fullCourseId = if (hasFullCourse) "course-${index + 100}" else null
            val description = when (index % 5) {
                0 -> "Learn the fundamentals of design thinking and create beautiful interfaces"
                1 -> "Master async programming with Kotlin's powerful coroutines API"
                2 -> "Explore modern Swift concurrency patterns and best practices"
                3 -> "Build scalable architectures using clean code principles"
                else -> "Tips and tricks for cross-platform development with Compose"
            }

            ReelsTable.insert { row ->
                row[ReelsTable.id] = "reel-$index"
                row[ReelsTable.title] = titles[index % titles.size]
                row[ReelsTable.videoUrl] = "https://storage.googleapis.com/exoplayer-test-media-1/mp4/android-screens-10s.mp4"
                row[ReelsTable.thumbnailUrl] = thumbs[index % thumbs.size]
                row[ReelsTable.authorName] = name
                row[ReelsTable.authorTitle] = title
                row[ReelsTable.likesCount] = 800 + random.nextInt(900)
                row[ReelsTable.commentsCount] = 20 + random.nextInt(120)
                row[ReelsTable.fullCourseId] = fullCourseId
                row[ReelsTable.description] = description
            }
        }
        Logger.i("Seeder", "Seeded reels")
    }

    private fun seedCourses() {
        val titles = listOf(
            "The Complete Guide to Your Ready Presentation",
            "Mastering Modern UI Storytelling",
            "Advanced UX Research For Creators",
            "AI Productivity For Course Teams"
        )
        val instructors = listOf(
            "Sarah Johnson",
            "Michael Alvarez",
            "Emily Chen",
            "Liam Walker"
        )
        val categories = listOf("Fitness", "Design", "Marketing", "Product")
        val durations = listOf("4 days 2 hrs", "8 hrs", "12 hrs", "3 days 6 hrs")
        val thumbs = listOf(
            "https://images.unsplash.com/photo-1517245386807-bb43f82c33c4?q=80&w=1900&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=1900&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1522202176988-66273c2fd55f?q=80&w=1900&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=1900&auto=format&fit=crop"
        )

        val total = 40
        (0 until total).forEach { index ->
            val random = Random(index)
            val hasDiscount = index % 2 == 0
            val currentPrice = 29.0 + random.nextDouble(10.0, 40.0)
            val originalPrice = if (hasDiscount) currentPrice * 1.8 else null
            val discountPercent = originalPrice?.let { (((it - currentPrice) / it) * 100).roundToInt() }

            CoursesTable.insert { row ->
                row[CoursesTable.id] = "course-$index"
                row[CoursesTable.title] = titles[index % titles.size]
                row[CoursesTable.instructorName] = instructors[index % instructors.size]
                row[CoursesTable.previewImageUrl] = thumbs[index % thumbs.size]
                row[CoursesTable.category] = categories[index % categories.size]
                row[CoursesTable.rating] = (38 + random.nextInt(20)) / 10.0
                row[CoursesTable.ratingCount] = 60 + random.nextInt(400)
                row[CoursesTable.lecturesCount] = 30 + random.nextInt(80)
                row[CoursesTable.durationText] = durations[index % durations.size]
                row[CoursesTable.currentPrice] = (currentPrice * 100).roundToInt() / 100.0
                row[CoursesTable.originalPrice] =
                    originalPrice?.let { price -> (price * 100).roundToInt() / 100.0 }
                row[CoursesTable.discountPercent] = discountPercent
            }
        }
        Logger.i("Seeder", "Seeded courses")
    }

    private fun seedProfiles() {
        ProfilesTable.insert {
            it[id] = "lecturer-1"
            it[fullName] = "Lecturer Name"
            it[title] = "Digital Marketing Lead"
            it[avatarUrl] = "https://images.unsplash.com/photo-1544723795-3fb6469f5b39?q=80&w=800&auto=format&fit=crop"
            it[isLecturer] = true
            it[students] = 2847
            it[courses] = 12
            it[rating] = 4.8
            it[followers] = 20_400
            it[following] = 320
            it[posts] = 96
            it[about] = "With over 10 years of experience in digital marketing, strategic marketing campaigns and data-driven practices."
        }
        Logger.i("Seeder", "Seeded profiles")
    }
}


