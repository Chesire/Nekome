package com.chesire.nekome.app.search.host.data

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * Wrapper around [SharedPreferences] to store settings or items related to Search.
 */
data class HostPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {

    var lastSearchGroup: String
        get() = sharedPreferences.getString(LAST_SEARCH_GROUP, "") ?: ""
        set(value) = sharedPreferences.edit { putString(LAST_SEARCH_GROUP, value) }

    companion object {
        private const val LAST_SEARCH_GROUP = "preference.last_search_group"
    }
}
