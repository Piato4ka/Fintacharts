package com.fintacharts.ctypto.domain

import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import com.fintacharts.ctypto.data.networking.dto.ResponseCoin
import com.fintacharts.ctypto.data.networking.dto.WebSocketDto
import kotlinx.coroutines.flow.Flow

interface CoinDataSource {
    suspend fun getCoin(): ResultLocal<ResponseCoin, NetworkError>
    suspend fun startWebSocket(): ResultLocal<Flow<WebSocketDto>, NetworkError>
}