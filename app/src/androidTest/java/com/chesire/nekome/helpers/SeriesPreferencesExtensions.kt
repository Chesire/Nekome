package com.chesire.nekome.helpers

import com.chesire.nekome.core.flags.SortOption
import com.chesire.nekome.core.preferences.SeriesPreferences

/**
 * Resets the [SeriesPreferences] back to a default state.
 */
suspend fun SeriesPreferences.reset() {
    updateFilter(
        mapOf(
            0 to true,
            1 to false,
            2 to false,
            3 to false,
            4 to false
        )
    )
    updateSort(SortOption.Default)
}
