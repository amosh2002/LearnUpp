package com.learnupp.data.auth

import com.learnupp.domain.model.AuthProvider
import com.learnupp.domain.repo.AuthRepository
import com.learnupp.util.Logger
import com.learnupp.util.SessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Apple
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SupabaseAuthRepository(
    private val client: SupabaseClient,
) : AuthRepository {

    private val auth = client.auth

    override suspend fun currentUserOrNull() = auth.currentUserOrNull()

    override suspend fun requestOtp(email: String): OtpRequestResult = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(OTP) {
                this.email = email
                createUser = true
            }
            // Supabase returns Unit on success
            OtpRequestResult(success = true, debugCode = null)
        } catch (t: Throwable) {
            Logger.e("SupabaseAuth", "requestOtp failed: ${t.message}")
            OtpRequestResult(success = false, debugCode = null)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): VerifyOtpResult? =
        withContext(Dispatchers.IO) {
            try {
                auth.verifyEmailOtp(
                    type = OtpType.Email.EMAIL,
                    email = email,
                    token = code
                )

                val session = auth.currentSessionOrNull()
                val user = auth.currentUserOrNull()

                if (session == null || user == null) {
                    Logger.e("SupabaseAuth", "verifyOtp: no active session")
                    return@withContext null
                }

                SessionManager.setTokens(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken ?: ""
                )

                // 1. Check Metadata (Fastest check)
                val metaFlag = user.userMetadata?.get("is_signup_complete")?.toString()?.toBoolean()

                // 2. Check DB (Fallback check)
                val fromProfiles = try {
                    client.from("profiles")
                        .select {
                            filter { eq("user_id", user.id) }
                            limit(1)
                        }
                        .decodeList<ProfileRow>()
                        .firstOrNull()
                } catch (e: Exception) {
                    null
                }

                val isComplete = (metaFlag == true) || (fromProfiles?.isSignUpComplete == true)
                // If user exists but has no username, force completion
                val requiresProfileCompletion =
                    !isComplete || fromProfiles?.username.isNullOrBlank()

                VerifyOtpResult(
                    tokens = AuthTokens(
                        accessToken = session.accessToken,
                        refreshToken = session.refreshToken ?: "",
                        expiresInSec = session.expiresIn?.toInt() ?: 3600, // Safe Long->Int cast
                        requiresProfileCompletion = requiresProfileCompletion,
                        userId = user.id,
                    )
                )
            } catch (t: Throwable) {
                Logger.e("SupabaseAuth", "verifyOtp failed: ${t.message}")
                null
            }
        }

    override suspend fun completeProfile(username: String, fullName: String?): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUserOrNull() ?: return@withContext false
                val normalizedUsername = username.trim().removePrefix("@")

                // 1. Update DB Row
                val updates = buildJsonObject {
                    put("username", normalizedUsername)
                    if (!fullName.isNullOrBlank()) {
                        put("full_name", fullName)
                    }
                    put("is_signup_complete", true)
                }

                client.from("profiles").update(updates) {
                    filter {
                        eq("user_id", currentUser.id)
                    }
                }

                // 2. Update Auth Metadata
                auth.updateUser {
                    data = buildJsonObject {
                        put("is_signup_complete", true)
                        put("username", normalizedUsername)
                        put("full_name", fullName ?: "")
                    }
                }

                SessionManager.setRequiresProfileCompletion(false)
                true
            } catch (t: Throwable) {
                Logger.e("SupabaseAuth", "completeProfile failed: ${t.message}")
                false
            }
        }

    override suspend fun isUsernameAvailable(username: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val normalized = username.trim().removePrefix("@")
                val rows = client.from("profiles")
                    .select {
                        filter { eq("username", normalized) }
                        limit(1)
                    }
                    .decodeList<ProfileRow>()
                rows.isEmpty()
            } catch (t: Throwable) {
                Logger.e("SupabaseAuth", "isUsernameAvailable failed: ${t.message}")
                false
            }
        }

    override suspend fun loginWithProvider(provider: AuthProvider): Boolean {
        return try {
            val supabaseProvider = when (provider) {
                AuthProvider.GOOGLE -> Google
                AuthProvider.APPLE -> Apple
            }
            // This opens the system browser
            auth.signInWith(supabaseProvider) {
                // Force account selection on Google to allow switching accounts
                if (supabaseProvider == Google) {
                    queryParams["prompt"] = "select_account"
                }
            }

            // Note: The app will close (go to background) here.
            // When the user finishes in the browser, the OS re-opens the app via Deep Link.
            // The Supabase library automatically catches the link and logs them in.
            true
        } catch (e: Exception) {
            Logger.e("Auth", "Login with Provider failed: ${e.message}")
            throw e
        }
    }

    override suspend fun isProfileIncomplete(): Boolean = withContext(Dispatchers.IO) {
        // (Your existing implementation here is correct)
        try {
            val user = auth.currentUserOrNull() ?: return@withContext false
            // 1. Check Metadata
            val metaFlag =
                user.userMetadata?.get("is_signup_complete")?.toString()?.toBoolean() == true
            if (metaFlag) return@withContext false

            // 2. Check DB
            val profile = client.from("profiles")
                .select { filter { eq("user_id", user.id) }; limit(1) }
                .decodeList<ProfileRow>()
                .firstOrNull()

            val isComplete = profile?.isSignUpComplete == true
            val hasUsername = !profile?.username.isNullOrBlank()

            !isComplete || !hasUsername
        } catch (e: Exception) {
            false
        }
    }

    // Unused methods
    override suspend fun logout(): Boolean {
        return try {
            auth.signOut()
            SessionManager.logout()
            true
        } catch (t: Throwable) {
            SessionManager.logout()
            false
        }
    }
}

// --- Data Models ---

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSec: Int,
    val requiresProfileCompletion: Boolean = false,
    val userId: String? = null,
)

data class OtpRequestResult(val success: Boolean, val debugCode: String? = null)
data class VerifyOtpResult(val tokens: AuthTokens)

@Serializable
data class ProfileRow(
    @SerialName("user_id") val userId: String,
    val username: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("is_signup_complete") val isSignUpComplete: Boolean = false,
    val about: String? = null,
    // Add count fields just in case
    @SerialName("students_count") val studentsCount: Int? = null,
    @SerialName("courses_count") val coursesCount: Int? = null,
    @SerialName("followers_count") val followersCount: Int? = null,
    @SerialName("following_count") val followingCount: Int? = null,
    @SerialName("posts_count") val postsCount: Int? = null,
    @SerialName("videos_count") val videosCount: Int? = null,
    @SerialName("reels_count") val reelsCount: Int? = null,
    val rating: Double? = null,
)