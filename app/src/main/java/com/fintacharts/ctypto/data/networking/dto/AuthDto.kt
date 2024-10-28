package com.fintacharts.ctypto.data.networking.dto

import kotlinx.serialization.Serializable


@Serializable
data class AuthDto(
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int,
    val refresh_token: String,
)