package com.fintacharts.core.domain.util


typealias DomainError = Error

sealed interface ResultLocal<out D, out E : Error> {
    data class Success<out D>(val data: D) : ResultLocal<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : ResultLocal<Nothing, E>
}

inline fun <T, E : Error, R> ResultLocal<T, E>.map(map: (T) -> R): ResultLocal<R, E> {
    return when (this) {
        is ResultLocal.Error -> ResultLocal.Error(error)
        is ResultLocal.Success -> ResultLocal.Success(map(data))
    }
}

fun <T, E : Error> ResultLocal<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { }
}

inline fun <T, E : Error> ResultLocal<T, E>.onSuccess(action: (T) -> Unit): ResultLocal<T, E> {
    return when (this) {
        is ResultLocal.Error -> this
        is ResultLocal.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E : Error> ResultLocal<T, E>.onError(action: (E) -> Unit): ResultLocal<T, E> {
    return when (this) {
        is ResultLocal.Error -> {
            action(error)
            this
        }

        is ResultLocal.Success -> this
    }
}

typealias EmptyResult<E> = ResultLocal<Unit, E>