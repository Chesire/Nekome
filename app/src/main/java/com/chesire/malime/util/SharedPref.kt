package com.chesire.malime.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.preferences.SortOption
import javax.inject.Inject

private const val preferencePrimaryService: String = "primaryService"
private const val preferenceAllowCrashReporting: String = "allowCrashReporting"
private const val preferenceFilterLength: String = "animeFilterLength"
private const val preferenceSeriesUpdateSchedulerEnabled: String = "seriesUpdateSchedulerEnabled"
const val preferenceFilter: String = "filter"
const val preferenceSort: String = "sort"

class SharedPref @Inject constructor(context: Context) {
    val sharedPrefFile: String = "malime_shared_pref"

    private val sharedPreferences =
        context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

    fun getPrimaryService(): SupportedService {
        return SupportedService.valueOf(sharedPreferences.getString(preferencePrimaryService, ""))
    }

    @SuppressLint("ApplySharedPref")
    fun putPrimaryService(service: SupportedService): SharedPref {
        // Push this into shared preferences straight away, instead of waiting in the background
        sharedPreferences.edit()
            .putString(preferencePrimaryService, service.name)
            .commit()

        return this
    }

    fun getAllowCrashReporting(): Boolean {
        return sharedPreferences.getBoolean(preferenceAllowCrashReporting, true)
    }

    fun getFilter(): BooleanArray {
        if (!hasStoredFilter()) {
            val defaultFilter = getDefaultFilter()
            setFilter(defaultFilter)
            return defaultFilter
        }

        val filterLength = sharedPreferences.getInt(preferenceFilterLength, 0)
        val returnArray = BooleanArray(filterLength)
        for (i in 0 until filterLength) {
            returnArray[i] = sharedPreferences.getBoolean(preferenceFilter + i, false)
        }

        return returnArray
    }

    fun setFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(preferenceFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(preferenceFilter + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getSortOption(): SortOption {
        return SortOption.getOptionFor(
            sharedPreferences.getInt(
                preferenceSort,
                SortOption.Title.id
            )
        )
    }

    fun setSortOption(sortOption: SortOption): SharedPref {
        sharedPreferences.edit()
            .putInt(preferenceSort, sortOption.id)
            .apply()

        return this
    }

    fun getSeriesUpdateSchedulerEnabled(): Boolean {
        return sharedPreferences.getBoolean(preferenceSeriesUpdateSchedulerEnabled, false)
    }

    fun setSeriesUpdateSchedulerEnabled(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(preferenceSeriesUpdateSchedulerEnabled, state)
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
        if (!sharedPreferences.contains(preferenceFilterLength)) {
            return false
        }

        val filterLength = sharedPreferences.getInt(preferenceFilterLength, 0)
        for (i in 0 until filterLength) {
            if (!sharedPreferences.contains(preferenceFilter + i)) {
                return false
            }
        }

        return true
    }

    private fun getDefaultFilter(): BooleanArray {
        return booleanArrayOf(true, false, false, false, false)
    }
}