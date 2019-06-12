package com.chesire.malime

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.flow.series.SortOption
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

/**
 * Provides a wrapper around the [SharedPreferences] to aid with getting and setting values into it.
 */
class SharedPref @Inject constructor(
    context: Context,
    private val sharedPreferences: SharedPreferences
) {
    private val _analyticsKey = context.getString(R.string.preference_analytics_enabled)

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
     * Preference value for if analytics have been enabled.
     */
    var isAnalyticsEnabled: Boolean
        get() = sharedPreferences.getBoolean(_analyticsKey, false)
        set(value) = sharedPreferences.edit { putBoolean(_analyticsKey, value) }

    /**
     * Preference value for if analytics has been completed.
     */
    var isAnalyticsComplete: Boolean
        get() = sharedPreferences.getBoolean(ANALYTICS_COMPLETE_PREFERENCE, false)
        set(value) = sharedPreferences.edit { putBoolean(ANALYTICS_COMPLETE_PREFERENCE, value) }

    /**
     * Subscribe to changes in the [SharedPreferences].
     */
    fun subscribeToChanges(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    /**
     * Unsubscribe from changes in the [SharedPreferences].
     */
    fun unsubscribeFromChanges(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        const val SORT_PREFERENCE = "preference.sort"
        const val FILTER_PREFERENCE = "preference.filter"
        const val ANALYTICS_COMPLETE_PREFERENCE = "preference.analytics.complete"
    }
}
