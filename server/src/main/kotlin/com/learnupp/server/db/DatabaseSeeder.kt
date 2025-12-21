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
                UsersTable,
                RefreshTokensTable,
                VideosTable,
                ReelsTable,
                CoursesTable,
                ProfilesTable,
                EarningsTransactionsTable,
                MessageCategoriesTable,
                MessageThreadsTable,
                NotificationSettingsTable,
                PaymentMethodsTable,
                LanguageOptionsTable
            )
        }
    }

    fun seedIfEmpty() {
        val database = db ?: return
        transaction(database) {
            if (UsersTable.selectAll().empty()) seedUsers()
            if (VideosTable.selectAll().empty()) seedVideos()
            if (ReelsTable.selectAll().empty()) seedReels()
            if (CoursesTable.selectAll().empty()) seedCourses()
            if (ProfilesTable.selectAll().empty()) seedProfiles()
            if (EarningsTransactionsTable.selectAll().empty()) seedEarnings()
            if (MessageCategoriesTable.selectAll().empty()) seedMessages()
            if (NotificationSettingsTable.selectAll().empty()) seedNotifications()
            if (PaymentMethodsTable.selectAll().empty()) seedPayments()
            if (LanguageOptionsTable.selectAll().empty()) seedLanguages()
        }
    }

    private fun seedUsers() {
        val now = java.time.Instant.now()
        UsersTable.insert {
            it[id] = "user-1"
            it[fullName] = "Test User"
            it[email] = "user@example.com"
            it[passwordHash] = com.learnupp.server.auth.PasswordManager.hash("pass")
            it[avatarUrl] = null
            it[createdAt] = now.epochSecond
        }
        Logger.i("Seeder", "Seeded users (user@example.com / pass)")
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

    private fun seedEarnings() {
        (0 until 5).forEach { index ->
            EarningsTransactionsTable.insert {
                it[id] = "tx-$index"
                it[title] = "Course Purchase: Sample ${index + 1}"
                it[date] = "Nov ${24 - index}, 2025"
                it[amount] = 25 + Random(index).nextDouble(10.0, 30.0)
            }
        }
        Logger.i("Seeder", "Seeded earnings transactions")
    }

    private fun seedMessages() {
        val avatars = listOf(
            "https://images.unsplash.com/photo-1544723795-3fb6469f5b39?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1525134479668-1bee5c7c6845?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?q=80&w=600&auto=format&fit=crop",
        )

        data class Cat(val id: String, val title: String, val desc: String, val iconText: String, val color: String)
        val categories = listOf(
            Cat("course-branding", "Course Name", "2 chats", "</>", "#B71C1C"),
            Cat("course-mobile", "Mobile UI Systems", "2 chats", "</>", "#D84315"),
            Cat("course-analytics", "Course Name", "2 chats", "{ }", "#6A1B9A"),
            Cat("ux-advanced", "Advanced UX Design", "1 group chat", "UX", "#1E88E5"),
        )
        categories.forEach { cat ->
            MessageCategoriesTable.insert {
                it[id] = cat.id
                it[title] = cat.title
                it[description] = cat.desc
                it[iconText] = cat.iconText
                it[iconColorHex] = cat.color
            }
        }

        // threads roughly mimic mocks
        MessageThreadsTable.insert {
            it[id] = "lecturer-1"
            it[categoryId] = "course-branding"
            it[title] = "Lecturer Name"
            it[subtitle] = "Lead Designer"
            it[lastMessageSnippet] = "Last message short"
            it[timestamp] = null
            it[isUnread] = true
            it[avatarUrl] = avatars[0]
            it[memberCount] = null
            it[type] = "DIRECT"
        }
        MessageThreadsTable.insert {
            it[id] = "group-1"
            it[categoryId] = "course-branding"
            it[title] = "Course Group"
            it[subtitle] = "Name: Last message short..."
            it[lastMessageSnippet] = "Sprint planning call on Friday"
            it[timestamp] = "4h"
            it[isUnread] = false
            it[avatarUrl] = avatars[1]
            it[memberCount] = 24
            it[type] = "GROUP"
        }
        MessageThreadsTable.insert {
            it[id] = "lecturer-2"
            it[categoryId] = "course-mobile"
            it[title] = "Lecturer Name"
            it[subtitle] = "Senior Android Engineer"
            it[lastMessageSnippet] = "Iteration build is ready"
            it[timestamp] = null
            it[isUnread] = true
            it[avatarUrl] = avatars[2]
            it[memberCount] = null
            it[type] = "DIRECT"
        }
        MessageThreadsTable.insert {
            it[id] = "ta-1"
            it[categoryId] = "course-mobile"
            it[title] = "Instructor Assistant"
            it[subtitle] = "Last message short"
            it[lastMessageSnippet] = "Uploading today's recording"
            it[timestamp] = null
            it[isUnread] = false
            it[avatarUrl] = avatars[3]
            it[memberCount] = null
            it[type] = "DIRECT"
        }
        MessageThreadsTable.insert {
            it[id] = "lecturer-3"
            it[categoryId] = "course-analytics"
            it[title] = "Lecturer Name"
            it[subtitle] = "Data Strategist"
            it[lastMessageSnippet] = "Last message short"
            it[timestamp] = null
            it[isUnread] = true
            it[avatarUrl] = avatars[0]
            it[memberCount] = null
            it[type] = "DIRECT"
        }
        MessageThreadsTable.insert {
            it[id] = "mentor-1"
            it[categoryId] = "course-analytics"
            it[title] = "Mentor Team"
            it[subtitle] = "Weekly sync summary"
            it[lastMessageSnippet] = "Slides are ready for review"
            it[timestamp] = null
            it[isUnread] = false
            it[avatarUrl] = avatars[1]
            it[memberCount] = null
            it[type] = "DIRECT"
        }
        MessageThreadsTable.insert {
            it[id] = "group-ux"
            it[categoryId] = "ux-advanced"
            it[title] = "Course Group"
            it[subtitle] = "Last message short..."
            it[lastMessageSnippet] = "Prototype feedback is uploaded"
            it[timestamp] = "11:32 AM"
            it[isUnread] = false
            it[avatarUrl] = avatars[2]
            it[memberCount] = 247
            it[type] = "GROUP"
        }
        Logger.i("Seeder", "Seeded messages")
    }

    private fun seedNotifications() {
        val data = listOf(
            Triple("push", "Push Notifications", null),
            Triple("email", "Email Notifications", null),
            Triple("sms", "SMS Notifications", null),
            Triple("course_updates", "Course Updates", null),
            Triple("comments", "Comments & Mentions", null),
            Triple("messages", "Messages", null),
            Triple("mute_all", "Mute All Notifications", "Temporarily pause all alerts"),
        )
        data.forEach { (idVal, titleVal, descVal) ->
            NotificationSettingsTable.insert {
                it[id] = idVal
                it[title] = titleVal
                it[description] = descVal
                it[enabled] = idVal != "sms" && idVal != "mute_all"
                it[group] = when (idVal) {
                    "push", "email", "sms" -> "GENERAL"
                    "course_updates", "comments", "messages" -> "APP_ACTIVITY"
                    "mute_all" -> "PREFERENCES"
                    else -> "GENERAL"
                }
            }
        }
        Logger.i("Seeder", "Seeded notifications")
    }

    private fun seedPayments() {
        val methods = listOf(
            PaymentMethodSeed("visa", "Visa Card", "Personal", "•••• 4821", "CARD", active = true, primary = true),
            PaymentMethodSeed("mastercard", "Mastercard •••• 9012", "Expires 12/25", "•••• 9012", "CARD", active = false, primary = false),
            PaymentMethodSeed("paypal", "john.doe@example.com", "PayPal Account", "", "PAYPAL", active = false, primary = false),
        )
        methods.forEach { m ->
            PaymentMethodsTable.insert {
                it[id] = m.id
                it[label] = m.label
                it[subtitle] = m.subtitle
                it[maskedNumber] = m.maskedNumber
                it[type] = m.type
                it[active] = m.active
                it[primary] = m.primary
            }
        }
        Logger.i("Seeder", "Seeded payments")
    }

    private fun seedLanguages() {
        val langs = listOf(
            LanguageSeed("en", "English", "\uD83C\uDDEC\uD83C\uDDE7", selected = true),
            LanguageSeed("ru", "Русский", "\uD83C\uDDF7\uD83C\uDDFA", selected = false),
            LanguageSeed("hy", "Հայերեն", "\uD83C\uDDE6\uD83C\uDDF2", selected = false),
        )
        langs.forEach { lang ->
            LanguageOptionsTable.insert {
                it[code] = lang.code
                it[name] = lang.name
                it[flagEmoji] = lang.flagEmoji
                it[selected] = lang.selected
            }
        }
        Logger.i("Seeder", "Seeded languages")
    }

    private data class PaymentMethodSeed(
        val id: String,
        val label: String,
        val subtitle: String,
        val maskedNumber: String,
        val type: String,
        val active: Boolean,
        val primary: Boolean,
    )

    private data class LanguageSeed(
        val code: String,
        val name: String,
        val flagEmoji: String,
        val selected: Boolean,
    )
}


