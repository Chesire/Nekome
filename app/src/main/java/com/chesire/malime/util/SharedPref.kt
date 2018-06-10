package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.util.sec.Decryptor
import com.chesire.malime.util.sec.Encryptor

private const val authAlias: String = "private_auth"
private const val preferenceAuth: String = "auth"
private const val preferenceUserId: String = "userId"
private const val preferenceUsername: String = "username"
private const val preferencePrimaryService: String = "primaryService"
private const val preferenceAllowCrashReporting: String = "allowCrashReporting"
private const val preferenceAnimeFilterLength: String = "animeFilterLength"
private const val preferenceAutoUpdateState: String = "autoUpdateState"
const val preferenceFilter: String = "filter"
const val preferenceSort: String = "sort"

class SharedPref(
    context: Context
) {
    val sharedPrefFile: String = "malime_shared_pref"

    private val sharedPreferences =
        context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    private val encryptor = Encryptor(context.applicationContext)
    private val decryptor = Decryptor()

    fun getAuth(): String {
        val text = sharedPreferences.getString(preferenceAuth, "")

        return if (text.isNotBlank()) {
            decryptor.decryptData(
                authAlias,
                Base64.decode(text, Base64.DEFAULT)
            )
        } else {
            ""
        }
    }

    fun putAuth(auth: String): SharedPref {
        val encrypted = encryptor.encryptText(authAlias, auth)

        sharedPreferences.edit()
            .putString(preferenceAuth, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()

        return this
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(preferenceUserId, 0)
    }

    fun putUserId(userId: Int): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceUserId, userId)
            .apply()

        return this
    }

    fun getUsername(): String {
        return sharedPreferences.getString(preferenceUsername, "")
    }

    fun putUsername(username: String): SharedPref {
        sharedPreferences.edit()
            .putString(preferenceUsername, username)
            .apply()

        return this
    }

    fun getPrimaryService(): SupportedService {
        return SupportedService.valueOf(sharedPreferences.getString(preferencePrimaryService, ""))
    }

    fun putPrimaryService(service: SupportedService): SharedPref {
        sharedPreferences.edit()
            .putString(preferencePrimaryService, service.name)
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
            returnArray[i] = sharedPreferences.getBoolean(preferenceFilter + i, false)
        }

        return returnArray
    }

    fun setAnimeFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(preferenceAnimeFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(preferenceFilter + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getSortOption(): Int {
        // If doesn't exist, return "Title"
        return sharedPreferences.getInt(preferenceSort, 1)
    }

    fun setSortOption(sortOption: Int): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceSort, sortOption)
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
            .remove(preferenceUserId)
            .remove(preferencePrimaryService)
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