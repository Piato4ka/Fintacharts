package com.fintacharts.ctypto.domain

interface SharedPrefsDataSource {
    fun setAccessToken(token: String)
    fun setRefreshToken(token: String)
}