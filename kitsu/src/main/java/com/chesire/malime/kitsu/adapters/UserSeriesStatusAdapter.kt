package com.chesire.malime.kitsu.adapters

import com.chesire.malime.server.flags.UserSeriesStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val CURRENT = "current"
private const val COMPLETED = "completed"
private const val ON_HOLD = "on_hold"
private const val DROPPED = "dropped"
private const val PLANNED = "planned"
private const val UNKNOWN = "unknown"

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

    @ToJson
    fun userSeriesStatusToString(status: UserSeriesStatus): String {
        return when (status) {
            UserSeriesStatus.Current -> CURRENT
            UserSeriesStatus.Completed -> COMPLETED
            UserSeriesStatus.OnHold -> ON_HOLD
            UserSeriesStatus.Dropped -> DROPPED
            UserSeriesStatus.Planned -> PLANNED
            else -> UNKNOWN
        }
    }
}
