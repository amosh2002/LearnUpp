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


