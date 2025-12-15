package com.learnupp.server.plugins

import com.learnupp.domain.model.Video
import com.learnupp.util.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            Logger.e("Routing", "Unhandled error: ${cause.message}")
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
    }

    routing {
        get("/health") {
            call.respond(mapOf("ok" to true))
        }

        // Minimal example endpoint proving we can share models between server + client:
        // - Client can deserialize the response using the same Video model from :sharedModel.
        route("/videos") {
            get {
                call.respond(emptyList<Video>())
            }

            post {
                val video = call.receive<Video>()
                call.respond(HttpStatusCode.Created, video)
            }
        }
    }
}


