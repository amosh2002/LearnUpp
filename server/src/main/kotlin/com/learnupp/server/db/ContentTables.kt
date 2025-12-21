package com.learnupp.server.db

import org.jetbrains.exposed.sql.Table

object VideosTable : Table("videos") {
    val id = varchar("id", 64)
    val title = varchar("title", 256)
    val previewImageUrl = varchar("preview_image_url", 512)
    val durationSec = integer("duration_sec")
    val authorName = varchar("author_name", 128)
    val channelName = varchar("channel_name", 128)
    val viewsCount = integer("views_count").default(0)
    val likesCount = integer("likes_count").default(0)
    val fullCourseId = varchar("full_course_id", 64).nullable()
    val fullVideoUrl = varchar("full_video_url", 512)

    override val primaryKey = PrimaryKey(id)
}

object ReelsTable : Table("reels") {
    val id = varchar("id", 64)
    val title = varchar("title", 256)
    val videoUrl = varchar("video_url", 512)
    val thumbnailUrl = varchar("thumbnail_url", 512)
    val authorName = varchar("author_name", 128)
    val authorTitle = varchar("author_title", 128)
    val likesCount = integer("likes_count").default(0)
    val commentsCount = integer("comments_count").default(0)
    val fullCourseId = varchar("full_course_id", 64).nullable()
    val description = text("description").nullable()

    override val primaryKey = PrimaryKey(id)
}

object CoursesTable : Table("courses") {
    val id = varchar("id", 64)
    val title = varchar("title", 256)
    val instructorName = varchar("instructor_name", 128)
    val previewImageUrl = varchar("preview_image_url", 512)
    val category = varchar("category", 128)
    val rating = double("rating")
    val ratingCount = integer("rating_count")
    val lecturesCount = integer("lectures_count")
    val durationText = varchar("duration_text", 128)
    val currentPrice = double("current_price")
    val originalPrice = double("original_price").nullable()
    val discountPercent = integer("discount_percent").nullable()

    override val primaryKey = PrimaryKey(id)
}

object ProfilesTable : Table("profiles") {
    val id = varchar("id", 64)
    val fullName = varchar("full_name", 128)
    val title = varchar("title", 128)
    val avatarUrl = varchar("avatar_url", 512).nullable()
    val isLecturer = bool("is_lecturer")
    val students = integer("students").default(0)
    val courses = integer("courses").default(0)
    val rating = double("rating").default(0.0)
    val followers = integer("followers").default(0)
    val following = integer("following").default(0)
    val posts = integer("posts").default(0)
    val about = text("about").default("")

    override val primaryKey = PrimaryKey(id)
}

object EarningsTransactionsTable : Table("earnings_transactions") {
    val id = varchar("id", 64)
    val title = varchar("title", 256)
    val date = varchar("date", 64)
    val amount = double("amount")
    override val primaryKey = PrimaryKey(id)
}

object MessageCategoriesTable : Table("message_categories") {
    val id = varchar("id", 64)
    val title = varchar("title", 128)
    val description = varchar("description", 256)
    val iconText = varchar("icon_text", 16)
    val iconColorHex = varchar("icon_color_hex", 16)
    override val primaryKey = PrimaryKey(id)
}

object MessageThreadsTable : Table("message_threads") {
    val id = varchar("id", 64)
    val categoryId = varchar("category_id", 64).index()
    val title = varchar("title", 128)
    val subtitle = varchar("subtitle", 256)
    val lastMessageSnippet = varchar("last_message_snippet", 256)
    val timestamp = varchar("timestamp", 64).nullable()
    val isUnread = bool("is_unread").default(false)
    val unreadCount = integer("unread_count").default(0)
    val avatarUrl = varchar("avatar_url", 512).nullable()
    val memberCount = integer("member_count").nullable()
    val type = varchar("type", 16)
    override val primaryKey = PrimaryKey(id)
}

object NotificationSettingsTable : Table("notification_settings") {
    val id = varchar("id", 64)
    val title = varchar("title", 128)
    val description = varchar("description", 256).nullable()
    val enabled = bool("enabled").default(true)
    val group = varchar("group_name", 32)
    override val primaryKey = PrimaryKey(id)
}

object PaymentMethodsTable : Table("payment_methods") {
    val id = varchar("id", 64)
    val label = varchar("label", 128)
    val subtitle = varchar("subtitle", 128)
    val maskedNumber = varchar("masked_number", 64)
    val type = varchar("type", 16)
    val active = bool("active").default(false)
    val primary = bool("primary").default(false)
    override val primaryKey = PrimaryKey(id)
}

object LanguageOptionsTable : Table("language_options") {
    val code = varchar("code", 8)
    val name = varchar("name", 64)
    val flagEmoji = varchar("flag_emoji", 8)
    val selected = bool("selected").default(false)
    override val primaryKey = PrimaryKey(code)
}


