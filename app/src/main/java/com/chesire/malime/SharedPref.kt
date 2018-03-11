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

    fun clearLoginDetails() {
        sharedPreferences.edit()
                .remove(preferenceAuth)
                .remove(preferenceUsername)
                .apply()
    }
}