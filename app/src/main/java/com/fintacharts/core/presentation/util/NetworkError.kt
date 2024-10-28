package com.fintacharts.core.presentation.util

import android.content.Context
import com.fintacharts.R
import com.fintacharts.core.domain.util.NetworkError


fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.REQUEST_TIMEOUT -> R.string.error_request_timeout
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.SERIALIZATION -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
        NetworkError.AUTH_ERROR -> R.string.error_auth
        NetworkError.WEB_SOCKET -> R.string.web_socket_error
    }
    return context.getString(resId)
}