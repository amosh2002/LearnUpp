package com.learnupp.server.db

import com.learnupp.domain.model.Course
import com.learnupp.domain.model.Profile
import com.learnupp.domain.model.Reel
import com.learnupp.domain.model.Video
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ContentRepository(private val db: Database) {

    val database: Database get() = db

    fun getVideos(page: Int, pageSize: Int): List<Video> = transaction(db) {
        val offset = (page * pageSize).toLong()
        VideosTable
            .selectAll()
            .limit(pageSize, offset = offset)
            .map { it.toVideo() }
    }

    fun getReels(page: Int, pageSize: Int): List<Reel> = transaction(db) {
        val offset = (page * pageSize).toLong()
        ReelsTable
            .selectAll()
            .limit(pageSize, offset = offset)
            .map { it.toReel() }
    }

    fun getCourses(page: Int, pageSize: Int): List<Course> = transaction(db) {
        val offset = (page * pageSize).toLong()
        CoursesTable
            .selectAll()
            .limit(pageSize, offset = offset)
            .map { it.toCourse() }
    }

    fun getProfile(): Profile? = transaction(db) {
        ProfilesTable
            .selectAll()
            .limit(1)
            .firstOrNull()
            ?.toProfile()
    }
}

private fun ResultRow.toVideo(): Video =
    Video(
        id = this[VideosTable.id],
        title = this[VideosTable.title],
        previewImageUrl = this[VideosTable.previewImageUrl],
        durationSec = this[VideosTable.durationSec],
        authorName = this[VideosTable.authorName],
        channelName = this[VideosTable.channelName],
        viewsCount = this[VideosTable.viewsCount],
        likesCount = this[VideosTable.likesCount],
        fullCourseId = this[VideosTable.fullCourseId],
        fullVideoUrl = this[VideosTable.fullVideoUrl],
    )

private fun ResultRow.toReel(): Reel =
    Reel(
        id = this[ReelsTable.id],
        title = this[ReelsTable.title],
        videoUrl = this[ReelsTable.videoUrl],
        thumbnailUrl = this[ReelsTable.thumbnailUrl],
        authorName = this[ReelsTable.authorName],
        authorTitle = this[ReelsTable.authorTitle],
        likesCount = this[ReelsTable.likesCount],
        commentsCount = this[ReelsTable.commentsCount],
        fullCourseId = this[ReelsTable.fullCourseId],
        description = this[ReelsTable.description],
    )

private fun ResultRow.toCourse(): Course =
    Course(
        id = this[CoursesTable.id],
        title = this[CoursesTable.title],
        instructorName = this[CoursesTable.instructorName],
        previewImageUrl = this[CoursesTable.previewImageUrl],
        category = this[CoursesTable.category],
        rating = this[CoursesTable.rating],
        ratingCount = this[CoursesTable.ratingCount],
        lecturesCount = this[CoursesTable.lecturesCount],
        durationText = this[CoursesTable.durationText],
        currentPrice = this[CoursesTable.currentPrice],
        originalPrice = this[CoursesTable.originalPrice],
        discountPercent = this[CoursesTable.discountPercent],
    )

private fun ResultRow.toProfile(): Profile =
    Profile(
        id = this[ProfilesTable.id],
        fullName = this[ProfilesTable.fullName],
        title = this[ProfilesTable.title],
        avatarUrl = this[ProfilesTable.avatarUrl],
        isLecturer = this[ProfilesTable.isLecturer],
        students = this[ProfilesTable.students],
        courses = this[ProfilesTable.courses],
        rating = this[ProfilesTable.rating],
        followers = this[ProfilesTable.followers],
        following = this[ProfilesTable.following],
        posts = this[ProfilesTable.posts],
        about = this[ProfilesTable.about],
    )


