package com.learnupp.data

/**
 * Base API URL for talking to the local Ktor server.
 * We use expect/actual so Android emulator can hit 10.0.2.2 while iOS/macOS can use localhost.
 */
expect val apiBaseUrl: String


