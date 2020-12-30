package com.chesire.nekome.seriesflags

/**
 * List of possible states a series can be in for a user.
 */
enum class UserSeriesStatus(val index: Int) {
    Unknown(-1),
    Current(0),
    Completed(1),
    OnHold(2),
    Dropped(3),
    Planned(4)
}
