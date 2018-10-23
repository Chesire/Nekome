package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.preferences.SortOption
import javax.inject.Inject

@Suppress("TooManyFunctions")
class SharedPref @Inject constructor(context: Context) {
    private val allowCrashReporting = context.getString(R.string.key_allow_crash_reporting)
    private val updateSchedulerEnabled = context.getString(R.string.key_update_scheduler_enabled)
    private val refreshSchedulerEnabled = context.getString(R.string.key_refresh_scheduler_enabled)
    private val forceBlockServices = context.getString(R.string.key_force_block_services)
    private val animeFilterLength = context.getString(R.string.key_anime_filter_length)
    private val _primaryService = context.getString(R.string.key_primary_service)
    private val filter = context.getString(R.string.key_filter)
    private val sort = context.getString(R.string.key_sort)

    private val sharedPreferences =
        context.getSharedPreferences(
            context.getString(R.string.key_shared_pref_file_name),
            Context.MODE_PRIVATE
        )

    var primaryService: SupportedService
        get() {
            val pref = sharedPreferences.getString(_primaryService, SupportedService.Unknown.name)
            return SupportedService.valueOf(pref)
        }
        set(service) {
            sharedPreferences.edit { it.put(_primaryService to service.name) }
        }

    fun getAllowCrashReporting() = sharedPreferences.getBoolean(allowCrashReporting, true)

    fun getFilter(): BooleanArray {
        if (!hasStoredFilter()) {
            val defaultFilter = getDefaultFilter()
            setFilter(defaultFilter)
            return defaultFilter
        }

        val filterLength = sharedPreferences.getInt(animeFilterLength, 0)
        val returnArray = BooleanArray(filterLength)
        for (i in 0 until filterLength) {
            returnArray[i] = sharedPreferences.getBoolean(filter + i, false)
        }

        return returnArray
    }

    fun setFilter(input: BooleanArray): SharedPref {
        val editor = sharedPreferences.edit()
        editor.putInt(animeFilterLength, input.count())
        for (i in input.indices) {
            editor.putBoolean(filter + i, input[i])
        }
        editor.apply()

        return this
    }

    fun getSortOption(): SortOption {
        return SortOption.getOptionFor(
            sharedPreferences.getInt(
                sort,
                SortOption.Title.id
            )
        )
    }

    fun setSortOption(sortOption: SortOption): SharedPref {
        sharedPreferences.edit()
            .putInt(sort, sortOption.id)
            .apply()

        return this
    }

    fun getSeriesUpdateSchedulerEnabled() =
        sharedPreferences.getBoolean(updateSchedulerEnabled, false)

    fun setSeriesUpdateSchedulerEnabled(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(updateSchedulerEnabled, state)
            .apply()

        return this
    }

    fun getRefreshTokenSchedulerEnabled() =
        sharedPreferences.getBoolean(refreshSchedulerEnabled, false)

    fun setRefreshTokenSchedulerEnabled(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(refreshSchedulerEnabled, state)
            .apply()

        return this
    }

    fun getForceBlockServices() = sharedPreferences.getBoolean(forceBlockServices, false)

    fun setForceBlockServices(state: Boolean): SharedPref {
        sharedPreferences.edit()
            .putBoolean(forceBlockServices, state)
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
        if (!sharedPreferences.contains(animeFilterLength)) {
            return false
        }

        val filterLength = sharedPreferences.getInt(animeFilterLength, 0)
        for (i in 0 until filterLength) {
            if (!sharedPreferences.contains(filter + i)) {
                return false
            }
        }

        return true
    }

    private fun getDefaultFilter() = booleanArrayOf(true, false, false, false, false)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        edit().apply { operation(this) }.apply()
    }

    private fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val (key, value) = pair

        when (value) {
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            else -> error("Invalid type ${value.javaClass} attempted to be passed into SharedPreferences")
        }
    }
}