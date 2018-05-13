package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.chesire.malime.util.sec.Decryptor
import com.chesire.malime.util.sec.Encryptor

private const val authSharedPrefFile: String = "private_auth"
private const val preferenceAuth: String = "auth"
private const val preferenceAuthIv: String = "authIv"
private const val preferenceUsername: String = "username"
private const val preferenceAllowCrashReporting: String = "allowCrashReporting"
private const val preferenceAnimeFilterLength: String = "animeFilterLength"
private const val preferenceAutoUpdateState: String = "autoUpdateState"

class SharedPref(
    context: Context
) {
    val preferenceAnimeFilter: String = "animeFilter"
    val preferenceAnimeSortOption: String = "animeSortOption"
    val sharedPrefFile: String = "malime_shared_pref"

    private val authSharedPreferences =
        context.getSharedPreferences(authSharedPrefFile, Context.MODE_PRIVATE)
    private val sharedPreferences =
        context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    private val encryptor = Encryptor()
    private val decryptor = Decryptor()

    fun getAuth(): String {
        val text = authSharedPreferences.getString(preferenceAuth, "")
        val iv = authSharedPreferences.getString(preferenceAuthIv, "")

        return if (text.isNotBlank() && iv.isNotBlank()) {
            decryptor.decryptData(
                authSharedPrefFile,
                Base64.decode(text, Base64.DEFAULT),
                Base64.decode(iv, Base64.DEFAULT)
            )
        } else {
            ""
        }
    }

    fun putAuth(auth: String): SharedPref {
        val encrypt = encryptor.encryptText(authSharedPrefFile, auth)

        authSharedPreferences.edit()
            .putString(preferenceAuth, Base64.encodeToString(encrypt.first, Base64.DEFAULT))
            .putString(preferenceAuthIv, Base64.encodeToString(encrypt.second, Base64.DEFAULT))
            .apply()

        return this
    }

    fun getUsername(): String {
        return authSharedPreferences.getString(preferenceUsername, "")
    }

    fun putUsername(username: String): SharedPref {
        authSharedPreferences.edit()
            .putString(preferenceUsername, username)
            .apply()

        return this
    }

    fun getAllowCrashReporting(): Boolean {
        return sharedPreferences.getBoolean(preferenceAllowCrashReporting, true)
    }

    fun getAnimeFilter(): BooleanArray {
        val filterLength = sharedPreferences.getInt(preferenceAnimeFilterLength, 0)
        if (filterLength == 0) {
            return getDefaultFilter()
        }

        val returnArray = BooleanArray(filterLength)
        for (i in 0 until filterLength) {
            returnArray[i] = sharedPreferences.getBoolean(preferenceAnimeFilter + i, false)
        }

        return returnArray
    }

    fun setAnimeFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(preferenceAnimeFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(preferenceAnimeFilter + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getAnimeSortOption(): Int {
        // If doesn't exist, return "Title"
        return sharedPreferences.getInt(preferenceAnimeSortOption, 1)
    }

    fun setAnimeSortOption(sortOption: Int): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceAnimeSortOption, sortOption)
            .apply()

        return this
    }

    fun getAutoUpdateSeriesState(): Boolean {
        return sharedPreferences.getBoolean(preferenceAutoUpdateState, false)
    }

    fun clearLoginDetails() {
        sharedPreferences.edit()
            .remove(preferenceAuth)
            .remove(preferenceUsername)
            .apply()
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun getDefaultFilter(): BooleanArray {
        return booleanArrayOf(true, false, false, false, false)
    }
}