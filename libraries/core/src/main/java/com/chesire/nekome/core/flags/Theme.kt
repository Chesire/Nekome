package com.chesire.nekome.core.flags

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.chesire.nekome.core.R

/**
 * Themes that the application can be to set to use.
 */
enum class Theme(val value: Int, @StringRes val stringId: Int) {
    System(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.settings_theme_system),
    Dark(AppCompatDelegate.MODE_NIGHT_YES, R.string.settings_theme_dark),
    Light(AppCompatDelegate.MODE_NIGHT_NO, R.string.settings_theme_light);

    companion object {
        /**
         * Gets a map of [value] to the string acquired from the [stringId].
         */
        fun getValueMap(context: Context) = values()
            .associate { it.value to context.getString(it.stringId) }

        /**
         * Gets the [Theme] from a given [value], the [value] should be the value field
         * in the the enum class, but as a string. If this is null or cannot be found then [System]
         * will be returned as a default.
         */
        fun fromValue(value: String): Theme {
            return value.toIntOrNull()?.let { intValue ->
                values().find { it.value == intValue } ?: System
            } ?: System
        }
    }
}
