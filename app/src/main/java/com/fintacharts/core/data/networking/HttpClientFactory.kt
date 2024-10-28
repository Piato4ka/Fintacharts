package com.fintacharts.core.data.networking

import android.util.Log
import com.fintacharts.core.data.local.SharedPrefsRepository
import com.fintacharts.core.domain.util.onError
import com.fintacharts.core.domain.util.onSuccess
import com.fintacharts.ctypto.data.networking.RemoteAuthDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


object HttpClientFactory {


    fun create(
        engine: HttpClientEngine,
        sharedPrefs: SharedPrefsRepository,
        authDataSource: RemoteAuthDataSource
    ): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(sharedPrefs.getAccessToken(), sharedPrefs.getRefreshToken())
                    }
                    refreshTokens {
                        val response = authDataSource.getAccessTokens(client)
                        response.onSuccess { authDto ->
                            sharedPrefs.setAccessToken(authDto.access_token)
                            sharedPrefs.setRefreshToken(authDto.refresh_token)
                        }.onError {
                            Log.e("CoinTag", it.toString())
                        }
                        BearerTokens(sharedPrefs.getAccessToken(), sharedPrefs.getRefreshToken())
                    }
                }
            }
            install(WebSockets)
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }

    }
}
