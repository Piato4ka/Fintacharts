package com.fintacharts.ctypto.domain

import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import com.fintacharts.ctypto.data.networking.dto.AuthDto
import io.ktor.client.HttpClient


interface AuthDataSource {
    suspend fun getAccessTokens(httpClient: HttpClient): ResultLocal<AuthDto, NetworkError>
}