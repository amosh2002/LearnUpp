package com.learnupp.data.profile

import com.learnupp.domain.model.Profile
import com.learnupp.util.Logger
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SupabaseProfileApi(
    private val client: SupabaseClient,
) : ProfileApi {
    override suspend fun fetchProfile(): Profile {
        return try {
            val user = client.auth.currentUserOrNull()
                ?: throw IllegalStateException("No Supabase user for profile fetch")

            Logger.d("ProfileApi", "Supabase GET /profiles for user=${user.id}")
            val rows = client.from("profiles")
                .select {
                    filter {
                        eq("user_id", user.id)
                    }
                    limit(1)
                }
                .decodeList<ProfileRow>()

            val row = rows.firstOrNull()
                ?: ProfileRow(userId = user.id, fullName = user.userMetadata?.get("full_name")?.toString() ?: "", title = "")

            Profile(
                id = row.userId,
                fullName = row.fullName ?: "",
                title = row.title ?: "",
                avatarUrl = row.avatarUrl,
                isLecturer = row.isLecturer ?: false,
                username = row.username,
                isSignUpComplete = row.isSignUpComplete,
                email = user.email,
                primaryLogin = null,
                linkedProviders = emptyList(),
                students = row.students ?: 0,
                courses = row.courses ?: 0,
                rating = row.rating ?: 0.0,
                followers = row.followers ?: 0,
                following = row.following ?: 0,
                posts = row.posts ?: 0,
                about = row.about ?: "",
            )
        } catch (t: Throwable) {
            Logger.e("ProfileApi", "Supabase profile failed: ${t.message}")
            throw t
        }
    }
}

@Serializable
private data class ProfileRow(
    @SerialName("user_id") val userId: String,
    @SerialName("full_name") val fullName: String? = null,
    val title: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("is_lecturer") val isLecturer: Boolean? = null,
    val username: String? = null,
    @SerialName("is_signup_complete") val isSignUpComplete: Boolean = false,
    val students: Int? = null,
    val courses: Int? = null,
    val rating: Double? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val posts: Int? = null,
    val about: String? = null,
)


