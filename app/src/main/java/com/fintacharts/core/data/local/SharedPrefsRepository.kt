package com.fintacharts.core.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SharedPrefsRepository(context: Context) {
    private var pref: SharedPreferences?
    private var editor: SharedPreferences.Editor?

    init {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        try {
            pref = EncryptedSharedPreferences.create(
                context,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            editor = pref?.edit()
        } catch (e: Exception) {
            Log.e("CoinTag", "Error creating shared prefs ${e.message.toString()}")
            pref = null
            editor = null
        }
    }

    private fun String.put(string: String) {
        editor?.putString(this, string)
        editor?.commit()
    }


   fun setAccessToken(token: String) {
        ACCESS_TOKEN.put(token)
    }

    fun setRefreshToken(token: String) {
        REFRESH_TOKEN.put(token)
    }

    fun getAccessToken(): String {
        return pref?.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun getRefreshToken(): String {
        return pref?.getString(REFRESH_TOKEN, "") ?: ""
    }


    companion object {
        private const val ACCESS_TOKEN = "com.fintacharts.access_token"
        private const val REFRESH_TOKEN = "com.fintacharts.refresh_token"
    }

}