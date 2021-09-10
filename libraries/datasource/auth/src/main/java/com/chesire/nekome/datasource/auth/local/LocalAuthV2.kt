package com.chesire.nekome.datasource.auth.local

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN_V2"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN_v2"

class LocalAuthV2 @Inject constructor(
    private val preferences: SharedPreferences
) : LocalAuth {

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
                putString(ACCESS_TOKEN, newRefreshToken)
            }
        }

    override fun clear() = preferences.edit {
        remove(ACCESS_TOKEN)
        remove(REFRESH_TOKEN)
    }
}
