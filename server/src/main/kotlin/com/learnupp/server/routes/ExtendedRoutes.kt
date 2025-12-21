package com.learnupp.server.routes

import com.learnupp.domain.model.earnings.EarningsSummary
import com.learnupp.domain.model.settings.NotificationSetting
import com.learnupp.server.db.ExtendedRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

@kotlinx.serialization.Serializable
data class UpdateNotificationRequest(val id: String, val enabled: Boolean)

fun Route.extendedRoutes(repo: ExtendedRepository?) {
    // Earnings
    route("/earnings") {
        get {
            val r = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            call.respond(r.getEarningsSummary())
        }
    }

    // Messages
    route("/messages") {
        get {
            val r = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            call.respond(r.getMessages())
        }
    }

    // Notifications
    route("/notifications") {
        get {
            val r = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            call.respond(r.getNotifications())
        }
        post("/update") {
            val r = repo ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            val req = call.receive<UpdateNotificationRequest>()
            call.respond(r.updateNotification(req.id, req.enabled))
        }
    }

    // Payment methods
    route("/payments") {
        get {
            val r = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            call.respond(r.getPayments())
        }
    }

    // Language options
    route("/languages") {
        get {
            val r = repo ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, "Database not configured")
            call.respond(r.getLanguages())
        }
    }
}


