package com.fintacharts.core.data.networking

import com.fintacharts.BuildConfig

fun constructUrl(url: String): String {
    return when {
        url.contains(BuildConfig.BASE_URL) -> url
        url.startsWith("/") -> BuildConfig.BASE_URL.dropLast(1) + url
        else -> BuildConfig.BASE_URL + url
    }
}