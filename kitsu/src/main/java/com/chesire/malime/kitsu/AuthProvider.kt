package com.chesire.malime.kitsu

import android.content.SharedPreferences

private const val ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
private const val REFRESH_TOKEN = "KEY_REFRESH_TOKEN"

class AuthProvider(private val preferences: SharedPreferences) {
    var accessToken: String
        get() = preferences.getString(ACCESS_TOKEN, "") ?: ""
        set(newAccessToken) = preferences.edit().putString(ACCESS_TOKEN, newAccessToken).apply()
    var refreshToken: String
        get() = preferences.getString(REFRESH_TOKEN, "") ?: ""
        set(newRefreshToken) = preferences.edit().putString(REFRESH_TOKEN, newRefreshToken).apply()
}
