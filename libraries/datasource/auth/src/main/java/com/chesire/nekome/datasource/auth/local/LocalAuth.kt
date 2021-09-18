package com.chesire.nekome.datasource.auth.local

import androidx.core.content.edit
import dagger.Reusable
import javax.inject.Inject

private const val ENCRYPTED_NEKOME_AUTH_PREF = "ENekomePrefStore"
private const val UNENCRYPTED_NEKOME_AUTH_PREF = "UneNekomePrefStore"
private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN_V2"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN_v2"

/**
 * Stores auth credentials in the shared preferences, encrypting it if possible.
 */
@Reusable
class LocalAuth @Inject constructor(preferenceProvider: PreferenceProvider) {

    private val preferences = preferenceProvider.retrievePreferences(
        ENCRYPTED_NEKOME_AUTH_PREF,
        UNENCRYPTED_NEKOME_AUTH_PREF
    )

    /**
     * Checks if there are credentials for this local auth.
     */
    val hasCredentials: Boolean
        get() = preferences.contains(ACCESS_TOKEN) || preferences.contains(REFRESH_TOKEN)

    /**
     * Access token to put into the api requests.
     */
    var accessToken: String
        get() = preferences.getString(ACCESS_TOKEN, "") ?: ""
        set(newAccessToken) {
            preferences.edit {
                putString(ACCESS_TOKEN, newAccessToken)
            }
        }

    /**
     * Refresh token to request a new access token.
     */
    var refreshToken: String
        get() = preferences.getString(REFRESH_TOKEN, "") ?: ""
        set(newRefreshToken) {
            preferences.edit {
                putString(REFRESH_TOKEN, newRefreshToken)
            }
        }

    /**
     * Clears out any currently stored auth tokens for this local auth.
     */
    fun clear() = preferences.edit {
        remove(ACCESS_TOKEN)
        remove(REFRESH_TOKEN)
    }
}
