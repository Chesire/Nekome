package com.chesire.nekome.datasource.auth.local

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.chesire.nekome.core.extensions.migrateTo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Provider for [SharedPreferences] instances.
 */
class PreferenceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Retrieves an instance of [SharedPreferences], providing an encrypted version if the SDK is
     * high enough.
     */
    fun retrievePreferences(
        encryptedStoreName: String,
        unencryptedStoreName: String
    ): SharedPreferences {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initEncryptedPreferences(encryptedStoreName)
                .also { encryptedPref ->
                    if (encryptedPref.all.isEmpty()) {
                        initUnencryptedPreferences(unencryptedStoreName).migrateTo(encryptedPref)
                    }
                }
        } else {
            initUnencryptedPreferences(unencryptedStoreName)
        }
    }

    private fun initEncryptedPreferences(storeName: String) =
        EncryptedSharedPreferences.create(
            storeName,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private fun initUnencryptedPreferences(storeName: String): SharedPreferences =
        context.getSharedPreferences(storeName, Context.MODE_PRIVATE)
}
