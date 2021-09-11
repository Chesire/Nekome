package com.chesire.nekome.datasource.auth.local

import androidx.core.content.edit
import dagger.Reusable
import javax.inject.Inject

private const val ENCRYPTED_NEKOME_AUTH_PREF = "ENekomePrefStore"
private const val UNENCRYPTED_NEKOME_AUTH_PREF = "UneNekomePrefStore"
private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN_V2"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN_v2"

/**
 * Stores auth credentials in the encrypted shares preferences.
 */
@Reusable
class LocalAuthV2 @Inject constructor(
    preferenceProvider: PreferenceProvider
) : LocalAuth {

    private val preferences = preferenceProvider.retrievePreferences(
        ENCRYPTED_NEKOME_AUTH_PREF,
        UNENCRYPTED_NEKOME_AUTH_PREF
    )

    override val hasCredentials: Boolean
        get() = preferences.contains(ACCESS_TOKEN) || preferences.contains(REFRESH_TOKEN)

    override var accessToken: String
        get() = preferences.getString(ACCESS_TOKEN, "") ?: ""
        set(newAccessToken) {
            preferences.edit {
                putString(ACCESS_TOKEN, newAccessToken)
            }
        }

    override var refreshToken: String
        get() = preferences.getString(REFRESH_TOKEN, "") ?: ""
        set(newRefreshToken) {
            preferences.edit {
                putString(REFRESH_TOKEN, newRefreshToken)
            }
        }

    override fun clear() = preferences.edit {
        remove(ACCESS_TOKEN)
        remove(REFRESH_TOKEN)
    }
}
