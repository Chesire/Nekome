package com.chesire.malime.util

import android.content.Context
import android.content.SharedPreferences
import com.chesire.malime.R
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.view.preferences.SortOption
import javax.inject.Inject

class SharedPref @Inject constructor(context: Context) {
    // private val allowCrashReporting = context.getString(R.string.key_allow_crash_reporting)
    private val _updateSchedulerEnabled = context.getString(R.string.key_update_scheduler_enabled)
    private val _refreshSchedulerEnabled = context.getString(R.string.key_refresh_scheduler_enabled)
    private val _forceBlockServices = context.getString(R.string.key_force_block_services)
    private val _primaryService = context.getString(R.string.key_primary_service)
    private val _animeFilterLength = context.getString(R.string.key_anime_filter_length)
    private val _filter = context.getString(R.string.key_filter)
    private val _sortOption = context.getString(R.string.key_sort)

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
        set(service) = sharedPreferences.edit { it.put(_primaryService to service.name) }

    var filter: BooleanArray
        get() {
            if (!hasStoredFilter()) {
                val defaultFilter = getDefaultFilter()
                filter = defaultFilter
                return defaultFilter
            }

            val filterLength = sharedPreferences.getInt(_animeFilterLength, 0)
            val returnArray = BooleanArray(filterLength)
            for (i in 0 until filterLength) {
                returnArray[i] = sharedPreferences.getBoolean(_filter + i, false)
            }

            return returnArray
        }
        set(value) {
            sharedPreferences.edit {
                it.put(_animeFilterLength to value.count())
                for (i in value.indices) {
                    it.put(_filter + i to value[i])
                }
            }
        }

    var sortOption: SortOption
        get() = SortOption.getOptionFor(sharedPreferences.getInt(_sortOption, SortOption.Title.id))
        set(option) = sharedPreferences.edit { it.put(_sortOption to option.id) }

    var seriesUpdateSchedulerEnabled: Boolean
        get() = sharedPreferences.getBoolean(_updateSchedulerEnabled, false)
        set(enabled) = sharedPreferences.edit { it.put(_updateSchedulerEnabled to enabled) }

    var refreshTokenSchedulerEnabled: Boolean
        get() = sharedPreferences.getBoolean(_refreshSchedulerEnabled, false)
        set(enabled) = sharedPreferences.edit { it.put(_refreshSchedulerEnabled to enabled) }

    var forceBlockServices: Boolean
        get() = sharedPreferences.getBoolean(_forceBlockServices, false)
        set(block) = sharedPreferences.edit { it.put(_forceBlockServices to block) }

    fun registerOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun hasStoredFilter(): Boolean {
        if (!sharedPreferences.contains(_animeFilterLength)) {
            return false
        }

        val filterLength = sharedPreferences.getInt(_animeFilterLength, 0)
        for (i in 0 until filterLength) {
            if (!sharedPreferences.contains(_filter + i)) {
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
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            else -> error("Invalid type ${value.javaClass} attempted to be passed into SharedPreferences")
        }
    }
}