package com.fintacharts.ctypto.data.networking

import com.fintacharts.core.data.local.SharedPrefsRepository
import com.fintacharts.core.data.networking.constructUrl
import com.fintacharts.core.data.networking.safeCall
import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import com.fintacharts.ctypto.data.networking.dto.ResponseCoin
import com.fintacharts.ctypto.data.networking.dto.WebSocketDto
import com.fintacharts.ctypto.domain.CoinDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

class RemoteCoinDataSource(
    private val httpClient: HttpClient,
    private val sharedPrefs: SharedPrefsRepository
) : CoinDataSource {
    override suspend fun getCoin(): ResultLocal<ResponseCoin, NetworkError> {
        return safeCall<ResponseCoin> {
            httpClient.get(
                constructUrl("/api/bars/v1/bars/date-range")
            ) {
                url {
                    parameters.append("interval", "2 ")
                    parameters.append("periodicity", "hour")
                    parameters.append("startDate", "2023-08-07")
                    parameters.append("instrumentId", "ebefe2c7-5ac9-43bb-a8b7-4a97bf2c2576")
                    parameters.append("endDate", "2023-08-08")
                    parameters.append("provider", "oanda")
                }
            }
        }
    }

    private var session: WebSocketSession? = null

    override suspend fun startWebSocket(): ResultLocal<Flow<WebSocketDto>, NetworkError> {
        val accessToken = sharedPrefs.getAccessToken()
        try {
            session = httpClient.webSocketSession {
                url(
                    "wss://platform.fintacharts.com/api/streaming/ws/v1/realtime?token=$accessToken"
                )
            }
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            return ResultLocal.Error(NetworkError.WEB_SOCKET)
        }
        session?.send(messageSocket)
        val messageStates = session!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                try {
                    val json = Json { ignoreUnknownKeys = true }
                    json.decodeFromString<WebSocketDto>(it.readText())
                } catch (e: SerializationException) {
                    println("Failed to parse message: ${e.localizedMessage}")
                    null
                }
            }
        return ResultLocal.Success(messageStates)
    }


}


const val messageSocket =
    "{   \"type\": \"l1-subscription\",   \"id\": \"1\",   \"instrumentId\": \"ad9e5345-4c3b-41fc-9437-1d253f62db52\",   \"provider\": \"simulation\",   \"subscribe\": true,   \"kinds\": [     \"ask\",     \"bid\",     \"last\"   ] }"