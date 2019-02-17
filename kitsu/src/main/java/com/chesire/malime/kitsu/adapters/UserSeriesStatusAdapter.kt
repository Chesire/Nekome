package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.UserSeriesStatus
import com.squareup.moshi.FromJson

private const val CURRENT = "current"
private const val COMPLETED = "completed"
private const val ON_HOLD = "on_hold"
private const val DROPPED = "dropped"
private const val PLANNED = "planned"

@Suppress("unused")
class UserSeriesStatusAdapter {
    @FromJson
    fun userSeriesStatusFromString(status: String): UserSeriesStatus {
        return when (status) {
            CURRENT -> UserSeriesStatus.Current
            COMPLETED -> UserSeriesStatus.Completed
            ON_HOLD -> UserSeriesStatus.OnHold
            DROPPED -> UserSeriesStatus.Dropped
            PLANNED -> UserSeriesStatus.Planned
            else -> UserSeriesStatus.Unknown
        }
    }
}
