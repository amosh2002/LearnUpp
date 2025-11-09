package com.learnupp.data.camera

import com.learnupp.domain.model.Camera
import com.learnupp.util.BASE_API_URL
import com.learnupp.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException


private const val TAG = "CameraApi"

interface CameraApi {
    suspend fun fetchAllCameras(): List<Camera>
    suspend fun fetchFeaturedCamera(): Camera
}

class KtorCameraApi(private val client: HttpClient) : CameraApi {

    override suspend fun fetchAllCameras(): List<Camera> {
        // Actual potential implementation using Ktor HTTP client
//        val urL = BASE_API_URL + "api/cameras"
//
//        return try {
//            Logger.d(TAG, "Getting all cameras from $urL")
//
//            val response: HttpResponse = client.get {
//                url(urL)
//            }
//
//            val responseBody = response.bodyAsText()
//            Logger.d(TAG, "Response: $responseBody")
//            Json.decodeFromString(ListSerializer(Camera.serializer()), responseBody)
//        } catch (e: Exception) {
//            if (e is CancellationException) throw e
//            e.printStackTrace()
//            emptyList()
//        }

//
        // Mock implementation, replace with actual API call
        return listOf(
            Camera("1", "Front Door", true),
            Camera("2", "Garage", true),
            Camera("3", "Backyard", false),
            Camera("4", "Living Room", true),
            Camera("5", "Kitchen", false),
            Camera("6", "Office", true),
            Camera("7", "Hallway", true),
            Camera("8", "Nursery", false),
            Camera("9", "Porch", true),
            Camera("10", "Driveway", true),
            Camera("11", "Garden", false),
            Camera("12", "Balcony", true),
        )
    }

    override suspend fun fetchFeaturedCamera(): Camera {
        // Mock implementation, replace with actual API call
        return Camera(
            id = "cam_featured",
            name = "Living Room",
            isLive = true,
            previewImageUrl = null,
        )
    }
}