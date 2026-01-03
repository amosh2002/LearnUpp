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
                // FIX: Use OtpType.Email.EMAIL (Standard for v3 SDK)
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

                // Sync tokens to your legacy SessionManager for UI flags
                SessionManager.setTokens(
                    accessToken = session.accessToken,
                    refreshToken = session.refreshToken ?: ""
                )

                // 1. Check Metadata (Fastest)
                // Safety: convert to string then boolean to handle JSON primitive types safely
                val metaFlag = user.userMetadata?.get("is_signup_complete")?.toString()?.toBoolean()

                // 2. Check DB (Fallback)
                val fromProfiles = try {
                    client.from("profiles")
                        .select {
                            filter { eq("user_id", user.id) }
                            limit(1)
                        }
                        .decodeList<ProfileRow>()
                        .firstOrNull()
                } catch (e: Exception) {
                    Logger.e("SupabaseAuth", "Failed to fetch profile: ${e.message}")
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

                // 1. Update 'profiles' table via update() to comply with RLS policy
                // We create a JSON object for partial update matching ProfileRow structure
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
                // FIX: Use buildJsonObject for correct JSON encoding
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

    // --- Legacy / Unused in OTP flow ---
    override suspend fun login(e: String, p: String): Boolean = false
    override suspend fun loginWithProvider(provider: AuthProvider): Boolean {
        return try {
            val supabaseProvider = when (provider) {
                AuthProvider.GOOGLE -> Google
                AuthProvider.APPLE -> Apple
            }
            // This opens the system browser
            auth.signInWith(supabaseProvider) {
                if (supabaseProvider == Google) {
                    // Force account selection on Google sign-in
                    queryParams["prompt"] = "select_account"
                }
            }

            // Note: The app will close (go to background) here.
            // When the user finishes in the browser, the OS re-opens the app via Deep Link.
            // The Supabase library automatically catches the link and logs them in.
            true
        } catch (e: Exception) {
            Logger.e("Auth", "Login failed: ${e.message}")
            throw e
        }
    }

    override suspend fun updateAbout(about: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val currentUser = auth.currentUserOrNull() ?: return@withContext false

            val updates = buildJsonObject {
                put("about", about)
            }

            client.from("profiles").update(updates) {
                filter {
                    eq("user_id", currentUser.id)
                }
            }
            true
        } catch (t: Throwable) {
            Logger.e("SupabaseAuth", "updateAbout failed: ${t.message}")
            false
        }
    }

    override suspend fun register(n: String, e: String, p: String, c: String): Boolean = false

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

    override suspend fun isProfileIncomplete(): Boolean = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUserOrNull() ?: return@withContext false
            // 1. Check Metadata
            val metaFlag =
                user.userMetadata?.get("is_signup_complete")?.toString()?.toBoolean() == true
            if (metaFlag) return@withContext false

            // 2. Check DB
            val profile = client.from("profiles")
                .select {
                    filter { eq("user_id", user.id) }
                    limit(1)
                }
                .decodeList<ProfileRow>()
                .firstOrNull()

            val isComplete = profile?.isSignUpComplete == true
            val hasUsername = !profile?.username.isNullOrBlank()

            !isComplete || !hasUsername
        } catch (e: Exception) {
            Logger.e("SupabaseAuth", "isProfileIncomplete check failed: ${e.message}")
            // Default to incomplete if check fails, to be safe? Or false to not block user?
            // Safer to return true if we suspect they might be incomplete, but let's default to false to avoid blocking on error if token is valid.
            // Actually, if we can't verify, we shouldn't block.
            false
        }
    }
}

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSec: Int,
    val requiresProfileCompletion: Boolean = false,
    val userId: String? = null,
)

data class OtpRequestResult(
    val success: Boolean,
    val debugCode: String? = null,
)

data class VerifyOtpResult(
    val tokens: AuthTokens,
)

// --- Supabase Row Models (Internal to Auth layer) ---

@Serializable
data class ProfileRow(
    val user_id: String,
    val username: String? = null,
    val full_name: String? = null,
    val is_signup_complete: Boolean = false,
    val about: String? = null,
    // Add count fields just in case they are needed here later, though primarily used for Auth checks
    val students_count: Int? = null,
    val courses_count: Int? = null,
    val followers_count: Int? = null,
    val following_count: Int? = null,
    val posts_count: Int? = null,
    val videos_count: Int? = null,
    val reels_count: Int? = null,
    val rating: Double? = null,
) {
    // Helper properties if you want camelCase usage in code
    val isSignUpComplete get() = is_signup_complete
    val fullName get() = full_name
}