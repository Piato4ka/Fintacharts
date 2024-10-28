package com.fintacharts.ctypto.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class ResponseCoin(
    val data: List<CoinData>
)

@Serializable
data class CoinData(
    val t: String,
    val o: Double
)

