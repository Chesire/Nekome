package com.chesire.malime

import android.content.Context
import com.chesire.malime.core.flags.UserSeriesStatus

fun UserSeriesStatus.getString(context: Context): String {
    val stringId = when (this) {
        UserSeriesStatus.Completed -> R.string.filter_state_completed
        UserSeriesStatus.Current -> R.string.filter_state_current
        UserSeriesStatus.Dropped -> R.string.filter_state_dropped
        UserSeriesStatus.OnHold -> R.string.filter_state_on_hold
        UserSeriesStatus.Planned -> R.string.filter_state_planned
        UserSeriesStatus.Unknown -> {
            // Unknown should never be seen
            R.string.filter_state_unknown
        }
    }

    return context.getString(stringId)
}

fun UserSeriesStatus.Companion.getSeriesStatusStrings(context: Context): Array<CharSequence> {
    return UserSeriesStatus.values()
        .filter { it != UserSeriesStatus.Unknown }
        .map { it.getString(context) }
        .toTypedArray()
}