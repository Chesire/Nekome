package com.chesire.nekome.core.flags

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
    Planned(4, R.string.filter_by_planned)
}
