package com.chesire.malime.kitsu

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN"

class AuthProvider @Inject constructor(
    private val preferences: SharedPreferences,
    private val cryption: Cryption
) {
    var accessToken: String
        get() = decryptData(preferences.getString(ACCESS_TOKEN, "") ?: "")
        set(newAccessToken) {
            preferences.edit {
                putString(ACCESS_TOKEN, encryptData(newAccessToken))
            }
        }

    var refreshToken: String
        get() = decryptData(preferences.getString(REFRESH_TOKEN, "") ?: "")
        set(newRefreshToken) {
            preferences.edit {
                putString(REFRESH_TOKEN, encryptData(newRefreshToken))
            }
        }

    fun clearAuth() {
        preferences.edit {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
        }
    }

    private fun decryptData(data: String) = if (data.isEmpty()) {
        ""
    } else {
        cryption.decryptData(cryption.base64Decrypt(data))
    }

    private fun encryptData(data: String) = cryption.base64Encrypt(cryption.encryptText(data))
}
