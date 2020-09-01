package com.chesire.nekome.app.series

import android.content.SharedPreferences
import androidx.core.content.edit
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Provides a wrapper around the [SharedPreferences] to aid with getting and setting values into it.
 */
class SeriesPreferences(private val sharedPreferences: SharedPreferences) {
    private val filterAdapter by lazy {
        Moshi.Builder()
            .build()
            .adapter<Map<Int, Boolean>>(
                Types.newParameterizedType(
                    Map::class.java,
                    Int::class.javaObjectType,
                    Boolean::class.javaObjectType
                )
            )
    }

    private val defaultFilter by lazy {
        filterAdapter.toJson(
            UserSeriesStatus
                .values()
                .filterNot { it == UserSeriesStatus.Unknown }
                .associate {
                    it.index to (it.index == 0)
                }
        )
    }

    /**
     * Preference value for the sort option.
     */
    var sortPreference: SortOption
        get() = SortOption.forIndex(
            sharedPreferences.getInt(
                SORT_PREFERENCE,
                SortOption.Default.index
            )
        )
        set(value) = sharedPreferences.edit {
            putInt(SORT_PREFERENCE, value.index)
        }

    /**
     * Preference value for the filter options.
     */
    var filterPreference: Map<Int, Boolean>
        get() {
            return filterAdapter.fromJson(
                sharedPreferences.getString(FILTER_PREFERENCE, defaultFilter) ?: defaultFilter
            ) ?: emptyMap()
        }
        set(value) = sharedPreferences.edit {
            putString(FILTER_PREFERENCE, filterAdapter.toJson(value))
        }

    /**
     * Subscribe to changes in the [SharedPreferences].
     */
    fun subscribeToChanges(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        const val SORT_PREFERENCE = "preference.sort"
        const val FILTER_PREFERENCE = "preference.filter"
    }
}
