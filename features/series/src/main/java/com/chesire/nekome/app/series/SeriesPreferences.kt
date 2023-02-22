package com.chesire.nekome.app.series

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "series")

/**
 * Provides a wrapper around the [SharedPreferences] to aid with getting and setting values into it.
 */
class SeriesPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context
) {

    private val rateOnCompletionKey = context.getString(R.string.key_rate_on_completion)

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

    val sort: Flow<SortOption> = context.dataStore.data.map { preferences ->
        SortOption.forIndex(preferences[sortPreferenceKey] ?: SortOption.Default.index)
    }

    suspend fun updateSort(sortOption: SortOption) {
        context.dataStore.edit { preferences ->
            preferences[sortPreferenceKey] = sortOption.index
        }
    }

    /**
     * Preference value for the sort option.
     */
    @Deprecated("Delete with the rest of the old series code")
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
     * Preference value for if a rating dialog should be displayed on completing a series.
     */
    var rateSeriesOnCompletion: Boolean
        get() = sharedPreferences.getBoolean(rateOnCompletionKey, false)
        set(value) = sharedPreferences.edit {
            putBoolean(rateOnCompletionKey, value)
        }

    /**
     * Subscribe to changes in the [SharedPreferences].
     */
    @Deprecated("Delete with the rest of the old series code")
    fun subscribeToChanges(changeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(changeListener)
    }

    companion object {
        const val SORT_PREFERENCE = "preference.sort"
        const val FILTER_PREFERENCE = "preference.filter"
        private val sortPreferenceKey = intPreferencesKey(SORT_PREFERENCE)
    }
}
