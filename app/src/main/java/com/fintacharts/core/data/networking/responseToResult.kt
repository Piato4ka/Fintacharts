package com.fintacharts.core.data.networking

import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): ResultLocal<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                ResultLocal.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                ResultLocal.Error(NetworkError.SERIALIZATION)
            }
        }

        401 -> ResultLocal.Error(NetworkError.AUTH_ERROR)
        408 -> ResultLocal.Error(NetworkError.REQUEST_TIMEOUT)
        in 500..599 -> ResultLocal.Error(NetworkError.SERVER_ERROR)
        else -> ResultLocal.Error(NetworkError.UNKNOWN)
    }
}