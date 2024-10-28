package com.fintacharts.core.data.networking

import com.fintacharts.core.domain.util.NetworkError
import com.fintacharts.core.domain.util.ResultLocal
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext


suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): ResultLocal<T, NetworkError> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return ResultLocal.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        return ResultLocal.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return ResultLocal.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}