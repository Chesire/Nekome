package com.chesire.nekome.kitsu.library.adapter

import com.chesire.nekome.seriesflags.UserSeriesStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val CURRENT = "current"
private const val COMPLETED = "completed"
private const val ON_HOLD = "on_hold"
private const val DROPPED = "dropped"
private const val PLANNED = "planned"
private const val UNKNOWN = "unknown"

/**
 * Adapter to plug into retrofit to convert strings into [UserSeriesStatus] values.
 */
class UserSeriesStatusAdapter {
    /**
     * Converts [status] into the [UserSeriesStatus] value.
     */
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

    /**
     * Converts [status] into its [String] value.
     */
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
