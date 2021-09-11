package com.chesire.nekome.datasource.auth.local

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context
) : LocalAuth {

    private val preferences: SharedPreferences

    init {
        preferences = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initEncryptedPreferences().also { encryptedPref ->
                if (encryptedPref.all.isEmpty()) {
                    initUnencryptedPreferences().migrateToEncrypted(encryptedPref)
                }
            }
        } else {
            initUnencryptedPreferences()
        }
    }

    private fun SharedPreferences.migrateToEncrypted(encryptedPreferences: SharedPreferences) {
        if (all.isEmpty()) {
            return
        }
        all.forEach { (key, value) ->
            if (value is String) {
                encryptedPreferences.edit {
                    putString(key, value)
                }
            }
        }
        edit {
            remove(ACCESS_TOKEN)
            remove(REFRESH_TOKEN)
        }
    }

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

    private fun initEncryptedPreferences(): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            ENCRYPTED_NEKOME_AUTH_PREF,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun initUnencryptedPreferences(): SharedPreferences =
        context.getSharedPreferences(UNENCRYPTED_NEKOME_AUTH_PREF, Context.MODE_PRIVATE)
}
