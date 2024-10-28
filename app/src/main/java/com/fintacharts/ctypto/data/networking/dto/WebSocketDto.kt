package com.fintacharts.ctypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class WebSocketDto(
    val type: String,
    val instrumentId: String? = null,
    val provider: String? = null,
    val bid: Bid? = null
)


@Serializable
data class Bid(
    val price: Double,
    val volume: Int
)