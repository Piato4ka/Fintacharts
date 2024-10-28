package com.fintacharts.ctypto.data.networking

import com.fintacharts.core.data.networking.constructUrl
import com.fintacharts.core.data.networking.safeCall
import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import com.fintacharts.ctypto.data.networking.dto.AuthDto
import com.fintacharts.ctypto.domain.AuthDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class RemoteAuthDataSource() : AuthDataSource {
    val realm = "fintatech"

    override suspend fun getAccessTokens(httpClient: HttpClient): ResultLocal<AuthDto, NetworkError> {
        return safeCall<AuthDto> {
            httpClient.submitForm(
                url = constructUrl("/identity/realms/$realm/protocol/openid-connect/token"),
                formParameters = parameters {
                    append("username", "r_test@fintatech.com")
                    append("password", "kisfiz-vUnvy9-sopnyv")
                    append("grant_type", "password")
                    append("client_id", "app-cli")
                }
            )
        }
    }


}