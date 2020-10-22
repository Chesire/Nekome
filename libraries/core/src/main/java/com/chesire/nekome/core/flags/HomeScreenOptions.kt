package com.chesire.nekome.core.flags

import android.content.Context
import androidx.annotation.StringRes
import com.chesire.nekome.core.R

/**
 * All possible types of a series.
 */
enum class HomeScreenOptions(val index: Int, @StringRes val stringId: Int) {
    Anime(0, R.string.anime),
    Manga(1, R.string.manga);

    companion object {
        /**
         * Gets a map of [index] to the string acquired from the [stringId].
         */
        fun getValueMap(context: Context) = HomeScreenOptions.values()
            .associate { it.index to context.getString(it.stringId) }

        /**
         * Gets the [UserSeriesStatus] from a given [index], the [index] should be the index field
         * in the the enum class, but as a string.
         */
        fun getFromIndex(index: String): HomeScreenOptions {
            return index.toIntOrNull()?.let { intIndex ->
                HomeScreenOptions.values().find { it.index == intIndex } ?: Anime
            } ?: Anime
        }
    }
}
