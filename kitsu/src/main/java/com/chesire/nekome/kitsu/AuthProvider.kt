package com.chesire.nekome.kitsu

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN"

/**
 * Provides authorization for Kitsu access.
 *
 * Auth is encrypted using [cryption], it is then stored in the [preferences] for access.
 */
class AuthProvider @Inject constructor(
    private val preferences: SharedPreferences,
    private val cryption: Cryption
) {
    /**
     * Access token to put into the Kitsu requests.
     */
    var accessToken: String
        get() = decryptedToken(preferences.getString(ACCESS_TOKEN, "") ?: "")
        set(newAccessToken) {
            preferences.edit {
                putString(ACCESS_TOKEN, cryption.encrypt(newAccessToken))
            }
        }

    /**
     * Refresh token to request a new access token.
     */
    var refreshToken: String
        get() = decryptedToken(preferences.getString(REFRESH_TOKEN, "") ?: "")
        set(newRefreshToken) {
            preferences.edit {
                putString(REFRESH_TOKEN, cryption.encrypt(newRefreshToken))
            }
        }

    private fun decryptedToken(preferenceValue: String): String {
        return if (preferenceValue.isNotEmpty()) {
            cryption.decrypt(preferenceValue)
        } else {
            ""
        }
    }

    /**
     * Clears out any currently stored auth tokens.
     */
    fun clearAuth() = preferences.edit {
        remove(ACCESS_TOKEN)
        remove(REFRESH_TOKEN)
    }
}
