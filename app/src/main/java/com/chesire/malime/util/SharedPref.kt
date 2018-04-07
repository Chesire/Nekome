package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Creates a wrapper around the shared preferences.
 * <p>
 * This allows the auth token and username to be written in and retrieved.
 * Note: This should later be modified to encrypt the auth before storing
 */
class SharedPref(
    context: Context
) {
    private val authSharedPrefFile: String = "private_auth"
    val sharedPrefFile: String = "malime_shared_pref"

    private val preferenceAuth: String = "auth"
    private val preferenceUsername: String = "username"
    private val preferenceAllowCrashReporting: String = "allowCrashReporting"
    private val preferenceAnimeFilterLength: String = "animeFilterLength"
    private val preferenceAutoUpdateState: String = "autoUpdateState"
    val preferenceAnimeFilter: String = "animeFilter"
    val preferenceAnimeSortOption: String = "animeSortOption"

    private val authSharedPreferences: SharedPreferences
    private val sharedPreferences: SharedPreferences

    init {
        authSharedPreferences =
                context.getSharedPreferences(authSharedPrefFile, Context.MODE_PRIVATE)
        sharedPreferences =
                context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    }

    fun getAuth(): String {
        return authSharedPreferences.getString(preferenceAuth, "")
    }

    fun putAuth(auth: String): SharedPref {
        authSharedPreferences.edit()
            .putString(preferenceAuth, auth)
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