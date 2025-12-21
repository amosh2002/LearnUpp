package com.learnupp.server.plugins

import com.learnupp.server.auth.DbAuthStore
import com.learnupp.server.auth.TokenService
import com.learnupp.server.auth.authRoutes
import com.learnupp.server.db.ContentRepository
import com.learnupp.server.routes.contentRoutes
import com.learnupp.server.routes.extendedRoutes
import com.learnupp.util.Logger
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(contentRepository: ContentRepository?) {
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

        val tokenService = TokenService(
            secret = System.getenv("JWT_SECRET") ?: "dev_secret_do_not_use_in_prod",
            issuer = System.getenv("JWT_ISSUER") ?: "https://learnupp.server",
            audience = System.getenv("JWT_AUDIENCE") ?: "learnupp-client",
            accessTtlSeconds = (System.getenv("ACCESS_TOKEN_TTL_SEC") ?: "600").toLong()
        )
        val db = contentRepository?.database
        val authStore = db?.let {
            DbAuthStore(
                db = it,
                refreshTtlSeconds = (System.getenv("REFRESH_TOKEN_TTL_SEC") ?: "${30L * 24 * 60 * 60}").toLong()
            )
        }

        authRoutes(store = authStore, tokenService = tokenService)

        contentRoutes(repo = contentRepository)
        extendedRoutes(repo = contentRepository?.let { com.learnupp.server.db.ExtendedRepository(it.database) })

        // Minimal example endpoint proving we can share models between server + client:
        // - Client can deserialize the response using the same Video model from :sharedModel.
    }
}


