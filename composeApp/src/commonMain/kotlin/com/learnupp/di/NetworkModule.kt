package com.learnupp.di

import com.learnupp.util.LocalizationService
import com.learnupp.util.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin
import com.learnupp.data.auth.AuthApi

val networkModule = module {
    single { jsonConfig() }

    /* clients ----------------------------------------------------------- */
    single<HttpClient>(Qualifiers.Plain) {
        buildPlainClient(
            json = get()
        )
    }

    single<HttpClient>(Qualifiers.Auth) {
        buildAuthClient(
            json = get()
        )
    }
}

object Qualifiers {
    val Plain = named("plainClient")
    val Auth = named("authClient")
}

// Re-use the same Json instance everywhere.
fun jsonConfig() = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

// Common timeout block so we don’t repeat the numbers.
fun HttpTimeout.HttpTimeoutCapabilityConfiguration.veryLong() {
    val ms = 500_000L
    requestTimeoutMillis = ms
    connectTimeoutMillis = ms
    socketTimeoutMillis = ms
}

// Thrown when the server answers 401
class UnauthorizedException(val status: HttpStatusCode) :
    RuntimeException("HTTP $status – unauthorized")

// ---------- 2. 2 tiny builder functions ----------

fun buildPlainClient(json: Json): HttpClient =
    HttpClient {
        install(HttpTimeout) { veryLong() }
        install(ContentNegotiation) { json(json) }

        val localizationService: LocalizationService = getKoin().get()
        val currentLanguage = localizationService.getCurrentLanguage()

        defaultRequest {
            header(HttpHeaders.Accept, ContentType.Application.Json)
            header("language-code", currentLanguage)
            contentType(ContentType.Application.Json)
        }
    }

private fun buildAuthClient(
    json: Json,
): HttpClient =
    HttpClient {
        /* shared stuff ---------------------------------------------------- */
        install(HttpTimeout) { veryLong() }
        install(ContentNegotiation) { json(json) }

        /* add headers to EVERY request ----------------------------------- */
        defaultRequest {
            // 🔄 fetch these values dynamically per request:
            val localizationService: LocalizationService = getKoin().get()
            val currentLanguage = localizationService.getCurrentLanguage()

            header(HttpHeaders.Accept, ContentType.Application.Json)
            header("language-code", currentLanguage)
            contentType(ContentType.Application.Json)
        }

        /* bearer auth with refresh ----------------------------------- */
        install(Auth) {
            bearer {
                loadTokens {
                    val access =
                        SessionManager.getCachedBearerToken() ?: SessionManager.getBearerToken()
                    val refresh =
                        SessionManager.getCachedRefreshToken() ?: SessionManager.getRefreshToken()
                    if (access != null && refresh != null) BearerTokens(access, refresh) else null
                }
                refreshTokens {
                    val refresh =
                        SessionManager.getCachedRefreshToken() ?: SessionManager.getRefreshToken()
                    if (refresh.isNullOrEmpty()) {
                        SessionManager.logout()
                        return@refreshTokens null
                    }
                    val authApi: AuthApi = getKoin().get()
                    val tokens = authApi.refresh(refresh)
                    if (tokens != null) {
                        SessionManager.setTokens(tokens.accessToken, tokens.refreshToken)
                        BearerTokens(tokens.accessToken, tokens.refreshToken)
                    } else {
                        SessionManager.logout()
                        null
                    }
                }
            }
        }

        /* catch 401 in ONE place ----------------------------------- */
        HttpResponseValidator {
            validateResponse { resp ->
                // let Ktor throw a ClientRequestException for 401
                if (resp.status == HttpStatusCode.Unauthorized) {
                    throw ClientRequestException(resp, resp.bodyAsText())
                }
            }

            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is ClientRequestException &&
                    cause.response.status == HttpStatusCode.Unauthorized
                ) {
                    // If refresh failed in Auth plugin, log out.
                    SessionManager.logout()
                }
            }
        }
    }
