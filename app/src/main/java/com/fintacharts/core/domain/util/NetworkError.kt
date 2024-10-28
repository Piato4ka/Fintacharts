package com.fintacharts.core.domain.util

enum class NetworkError : Error {
    REQUEST_TIMEOUT,
    AUTH_ERROR,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    WEB_SOCKET,
    UNKNOWN,
}