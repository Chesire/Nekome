package com.chesire.malime.util.extension

import android.content.Context
import com.chesire.malime.R
import com.chesire.malime.core.flags.SeriesStatus

fun SeriesStatus.getString(context: Context): String {
    val stringId = when (this) {
        SeriesStatus.Current -> R.string.series_state_current
        SeriesStatus.Finished -> R.string.series_state_finished
        SeriesStatus.TBA -> R.string.series_state_tba
        SeriesStatus.Unreleased -> R.string.series_state_unreleased
        SeriesStatus.Upcoming -> R.string.series_state_upcoming
        SeriesStatus.Unknown -> {
            // Unknown should never be seen
            R.string.series_state_unknown
        }
    }

    return context.getString(stringId)
}