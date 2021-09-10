package com.chesire.nekome.datasource.auth.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.chesire.nekome.encryption.Cryption
import javax.inject.Inject

private const val ACCESS_TOKEN = "KEY_KITSU_ACCESS_TOKEN"
private const val REFRESH_TOKEN = "KEY_KITSU_REFRESH_TOKEN"
private const val ALIAS = "kitsuPrivateAuth"

@Deprecated("This is legacy and will be removed in a later release once migration has completed")
class LocalAuthV1 @Inject constructor(
    private val preferences: SharedPreferences,
    private val cryption: Cryption
) : LocalAuth {

    override val hasCredentials: Boolean
        get() = preferences.contains(ACCESS_TOKEN) || preferences.contains(REFRESH_TOKEN)

    override var accessToken: String
        get() {
            val token = preferences.getString(ACCESS_TOKEN, null)
            return if (token == null) {
                ""
            } else {
                decryptToken(token)
            }
        }
        set(newAccessToken) {
            preferences.edit {
                putString(ACCESS_TOKEN, cryption.encrypt(newAccessToken, ALIAS))
            }
        }

    override var refreshToken: String
        get() {
            val token = preferences.getString(REFRESH_TOKEN, null)
            return if (token == null) {
                ""
            } else {
                decryptToken(token)
            }
        }
        set(newRefreshToken) {
            preferences.edit {
                putString(REFRESH_TOKEN, cryption.encrypt(newRefreshToken, ALIAS))
            }
        }

    override fun clear() = preferences.edit {
        remove(ACCESS_TOKEN)
        remove(REFRESH_TOKEN)
    }

    private fun decryptToken(preferenceValue: String): String {
        return try {
            if (preferenceValue.isNotEmpty()) {
                cryption.decrypt(preferenceValue, ALIAS)
            } else {
                ""
            }
        } catch (ex: Exception) {
            ""
        }
    }
}
