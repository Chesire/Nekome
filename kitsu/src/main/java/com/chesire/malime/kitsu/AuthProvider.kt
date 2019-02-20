package com.chesire.malime.kitsu

import android.content.SharedPreferences
import android.util.Base64

private const val ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
private const val REFRESH_TOKEN = "KEY_REFRESH_TOKEN"

class AuthProvider(
    private val preferences: SharedPreferences,
    private val cryption: Cryption
) {
    var accessToken: String
        get() = decryptData(preferences.getString(ACCESS_TOKEN, "") ?: "")
        set(newAccessToken) {
            preferences.edit()
                .putString(ACCESS_TOKEN, encryptData(newAccessToken))
                .apply()
        }

    var refreshToken: String
        get() = decryptData(preferences.getString(REFRESH_TOKEN, "") ?: "")
        set(newRefreshToken) {
            preferences.edit()
                .putString(REFRESH_TOKEN, encryptData(newRefreshToken))
                .apply()
        }

    private fun decryptData(data: String): String {
        return if (data.isEmpty()) {
            ""
        } else {
            cryption.decryptData(Base64.decode(data, Base64.DEFAULT))
        }
    }

    private fun encryptData(data: String) =
        Base64.encodeToString(cryption.encryptText(data), Base64.DEFAULT)
}
