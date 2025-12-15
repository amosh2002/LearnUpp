package com.learnupp.server

import com.learnupp.server.plugins.configureFirebase
import com.learnupp.server.plugins.configureRouting
import com.learnupp.server.plugins.configureSecurity
import com.learnupp.server.plugins.configureSerialization
import com.learnupp.server.plugins.tryConfigureDatabases
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        factory = Netty,
        port = (System.getenv("PORT") ?: "8080").toInt(),
        host = System.getenv("HOST") ?: "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    // Keep server startup resilient in dev: plugins should not crash if env isn't configured yet.
    configureSerialization()
    tryConfigureDatabases()
    configureSecurity()
    configureFirebase()
    configureRouting()
}


