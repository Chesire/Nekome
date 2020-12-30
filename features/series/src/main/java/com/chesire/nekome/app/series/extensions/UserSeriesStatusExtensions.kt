package com.chesire.nekome.app.series.extensions

import android.content.Context
import androidx.annotation.StringRes
import com.chesire.nekome.app.series.R
import com.chesire.nekome.seriesflags.UserSeriesStatus

/**
 * Extra methods for the [UserSeriesStatus] class.
 */
object UserSeriesStatusExtensions {
    private val UserSeriesStatus.stringId: Int
        @StringRes
        get() = when (this) {
            UserSeriesStatus.Unknown -> 0
            UserSeriesStatus.Current -> R.string.filter_by_current
            UserSeriesStatus.Completed -> R.string.filter_by_completed
            UserSeriesStatus.OnHold -> R.string.filter_by_on_hold
            UserSeriesStatus.Dropped -> R.string.filter_by_dropped
            UserSeriesStatus.Planned -> R.string.filter_by_planned
        }

    /**
     * Gets a map of the [UserSeriesStatus] index to the string acquired from the [stringId].
     * This call will ignore the [Unknown] value as it should not be displayed to the user.
     */
    fun getValueMap(context: Context) = UserSeriesStatus.values()
        .filterNot { it == UserSeriesStatus.Unknown }
        .associate { it.index to context.getString(it.stringId) }
}
