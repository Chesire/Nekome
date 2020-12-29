package com.chesire.nekome.app.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.chesire.nekome.seriesflags.SeriesType
import javax.inject.Inject

/**
 * Wrapper around [SharedPreferences] to store settings or items related to Search.
 */
@Suppress("UseDataClass")
class SearchPreferences @Inject constructor(private val sharedPreferences: SharedPreferences) {
    /**
     * The last type that was selected on the Search screen, defaults to Unknown if none found.
     */
    var lastSearchType: Int
        get() = sharedPreferences.getInt(LAST_SEARCH_TYPE, SeriesType.Unknown.id)
        set(value) = sharedPreferences.edit { putInt(LAST_SEARCH_TYPE, value) }

    companion object {
        private const val LAST_SEARCH_TYPE = "preference.last_search_type"
    }
}
