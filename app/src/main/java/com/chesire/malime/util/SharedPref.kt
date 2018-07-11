package com.chesire.malime.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.preferences.SortOption
import javax.inject.Inject

private const val PREF_PRIMARY_SERVICE: String = "primaryService"
private const val PREF_ALLOW_CRASH_REPORTING: String = "allowCrashReporting"
private const val PREF_FILTER_LENGTH: String = "animeFilterLength"
private const val PREF_SERIES_UPDATE_SCHEDULER_ENABLED: String = "seriesUpdateSchedulerEnabled"
const val PREF_FILTER: String = "filter"
const val PREF_SORT: String = "sort"
const val SHARED_PREF_FILE = "malime_shared_pref"

@Suppress("TooManyFunctions")
class SharedPref @Inject constructor(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)

    fun getPrimaryService() =
        SupportedService.valueOf(sharedPreferences.getString(PREF_PRIMARY_SERVICE, ""))

    @SuppressLint("ApplySharedPref")
    fun putPrimaryService(service: SupportedService): SharedPref {
        // Push this into shared preferences straight away, instead of waiting in the background
        sharedPreferences.edit()
            .putString(PREF_PRIMARY_SERVICE, service.name)
            .commit()

        return this
    }

    fun getAllowCrashReporting() = sharedPreferences.getBoolean(PREF_ALLOW_CRASH_REPORTING, true)

    fun getFilter(): BooleanArray {
        if (!hasStoredFilter()) {
            val defaultFilter = getDefaultFilter()
            setFilter(defaultFilter)
            return defaultFilter
        }

        val filterLength = sharedPreferences.getInt(PREF_FILTER_LENGTH, 0)
        val returnArray = BooleanArray(filterLength)
        for (i in 0 until filterLength) {
            returnArray[i] = sharedPreferences.getBoolean(PREF_FILTER + i, false)
        }

        return returnArray
    }

    fun setFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(PREF_FILTER_LENGTH, input.count())
        for (i in input.indices) {
            editor.putBoolean(PREF_FILTER + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getSortOption(): SortOption {
        return SortOption.getOptionFor(
            sharedPreferences.getInt(
                PREF_SORT,
                SortOption.Title.id
            )
        )
    }

    fun setSortOption(sortOption: SortOption): SharedPref {
        sharedPreferences.edit()
            .putInt(PREF_SORT, sortOption.id)
            .apply()

        return this
    }

    fun getSeriesUpdateSchedulerEnabled() =
        sharedPreferences.getBoolean(PREF_SERIES_UPDATE_SCHEDULER_ENABLED, false)

    fun setSeriesUpdateSchedulerEnabled(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(PREF_SERIES_UPDATE_SCHEDULER_ENABLED, state)
            .apply()

        return this
    }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun hasStoredFilter(): Boolean {
        if (!sharedPreferences.contains(PREF_FILTER_LENGTH)) {
            return false
        }

        val filterLength = sharedPreferences.getInt(PREF_FILTER_LENGTH, 0)
        for (i in 0 until filterLength) {
            if (!sharedPreferences.contains(PREF_FILTER + i)) {
                return false
            }
        }

        return true
    }

    private fun getDefaultFilter() = booleanArrayOf(true, false, false, false, false)
}