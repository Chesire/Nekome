package com.chesire.nekome.core.flags

import android.content.Context
import androidx.annotation.StringRes
import com.chesire.nekome.core.R

/**
 * List of possible states a series can be in for a user.
 */
enum class UserSeriesStatus(val index: Int, @StringRes val stringId: Int) {
    Unknown(-1, 0),
    Current(0, R.string.filter_by_current),
    Completed(1, R.string.filter_by_completed),
    OnHold(2, R.string.filter_by_on_hold),
    Dropped(3, R.string.filter_by_dropped),
    Planned(4, R.string.filter_by_planned);

    companion object {
        /**
         * Gets a map of [index] to the string acquired from the [stringId].
         * This call will ignore the [Unknown] value as it should not be displayed to the user.
         */
        fun getValueMap(context: Context) = values()
            .filterNot { it == Unknown }
            .associate { it.index to context.getString(it.stringId) }
    }
}
