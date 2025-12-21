package com.learnupp.server.routes

import com.learnupp.server.db.ContentRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.contentRoutes(repo: ContentRepository?) {
    route("/videos") {
        get {
            val repository = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            call.respond(repository.getVideos(page, pageSize))
        }
    }

    route("/reels") {
        get {
            val repository = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            call.respond(repository.getReels(page, pageSize))
        }
    }

    route("/courses") {
        get {
            val repository = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
            val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            call.respond(repository.getCourses(page, pageSize))
        }
    }

    route("/profile") {
        get {
            val repository = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val profile = repository.getProfile()
            if (profile != null) {
                call.respond(profile)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}


