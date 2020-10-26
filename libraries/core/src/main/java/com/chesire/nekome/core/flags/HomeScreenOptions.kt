package com.chesire.nekome.core.flags

import android.content.Context
import androidx.annotation.StringRes
import com.chesire.nekome.core.R

/**
 * Options for default home screen.
 */
enum class HomeScreenOptions(val index: Int, @StringRes val stringId: Int) {
    Anime(0, R.string.nav_anime),
    Manga(1, R.string.nav_manga);

    companion object {
        /**
         * Gets a map of [index] to the string acquired from the [stringId].
         */
        fun getValueMap(context: Context) = HomeScreenOptions.values()
            .associate { it.index to context.getString(it.stringId) }

        /**
         * Gets the [HomeScreenOptions] from a given [index], the [index] should be the index field.
         * in the the enum class, but as a string.
         */
        fun getFromIndex(index: String): HomeScreenOptions {
            return index.toIntOrNull()?.let { intIndex ->
                HomeScreenOptions.values().find { it.index == intIndex } ?: Anime
            } ?: Anime
        }
    }
}
