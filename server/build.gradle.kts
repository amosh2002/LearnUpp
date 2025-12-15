plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinxSerialization)
    application
}

configurations.configureEach {
    // Firebase Admin / Google Cloud deps pull a "dummy" listenablefuture artifact to avoid old conflicts.
    // With Gradle capabilities, that dummy artifact can still conflict with Guava.
    exclude(group = "com.google.guava", module = "listenablefuture")

    resolutionStrategy {
        // Ensure a single Guava version is selected for the whole runtimeClasspath.
        force("com.google.guava:guava:32.1.1-jre")
    }
}

dependencies {
    // Shared contract (models, DTOs if we later add them)
    implementation(project(":sharedModel"))

    // Explicitly add Guava so dependency resolution picks a single, known-good version.
    implementation(libs.guava)

    // Ktor server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.cors)

    // Auth (JWT) + password hashing (for later phases)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.bcrypt)

    // Database (for later phases)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql.driver)
    implementation(libs.hikaricp)

    // Logging + external services
    implementation(libs.logback.classic)
    implementation(libs.firebase.admin)
}

application {
    mainClass.set("com.learnupp.server.ApplicationKt")
}


