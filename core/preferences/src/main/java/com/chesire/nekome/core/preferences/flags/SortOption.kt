package com.chesire.nekome.core.preferences.flags

import androidx.annotation.StringRes
import com.chesire.nekome.resources.StringResource

/**
 * Options available for when sorting the series list is performed.
 */
enum class SortOption(val index: Int, @StringRes val stringId: Int) {
    Default(0, StringResource.sort_by_default),
    Title(1, StringResource.sort_by_title),
    StartDate(2, StringResource.sort_by_start_date),
    EndDate(3, StringResource.sort_by_end_date),
    Rating(4, StringResource.sort_by_rating);

    companion object {
        /**
         * Get [SortOption] for its given [index].
         */
        fun forIndex(index: Int): SortOption = values().find { it.index == index } ?: Default
    }
}
