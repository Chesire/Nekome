package com.chesire.malime.extensions

import androidx.annotation.StringRes
import com.chesire.malime.R
import com.chesire.malime.server.flags.UserSeriesStatus

/**
 * StringID used for getting a string representing the status as a readable string.
 */
val UserSeriesStatus.stringId: Int
    @StringRes
    get() {
        return when (this) {
            UserSeriesStatus.Current -> R.string.filter_by_current
            UserSeriesStatus.Completed -> R.string.filter_by_completed
            UserSeriesStatus.OnHold -> R.string.filter_by_on_hold
            UserSeriesStatus.Dropped -> R.string.filter_by_dropped
            UserSeriesStatus.Planned -> R.string.filter_by_planned
            UserSeriesStatus.Unknown -> error("Unknown UserSeriesStatus requested")
        }
    }
