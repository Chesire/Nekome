package com.chesire.malime

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
    private val sharedPrefFile: String = "private_auth"
    private val preferenceAuth: String = "auth"
    private val preferenceUsername: String = "username"
    val preferenceAnimeFilter: String = "animeFilter"
    private val preferenceAnimeFilterLength: String = "animeFilterLength"

    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
    }

    fun getAuth(): String {
        return sharedPreferences.getString(preferenceAuth, "")
    }

    fun putAuth(auth: String): SharedPref {
        sharedPreferences.edit()
            .putString(preferenceAuth, auth)
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

    fun setAnimeFilter(input: BooleanArray) {
        val editor = sharedPreferences.edit()
        editor.putInt(preferenceAnimeFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(preferenceAnimeFilter + i, input[i])
        }
        editor.apply()
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