package com.learnupp.server.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.learnupp.util.Logger
import io.ktor.server.application.Application
import java.io.FileInputStream

/**
 * Firebase Admin is optional in dev. Configure it by setting FIREBASE_CREDENTIALS
 * to a service account JSON path (do not commit credentials).
 */
fun Application.configureFirebase() {
    val serviceAccountPath = System.getenv("FIREBASE_CREDENTIALS") ?: return

    try {
        FileInputStream(serviceAccountPath).use { serviceAccount ->
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            }
        }

        Logger.i("Firebase", "Firebase initialized")
    } catch (t: Throwable) {
        Logger.w("Firebase", "Failed to initialize Firebase (continuing without it): ${t.message}")
    }
}


