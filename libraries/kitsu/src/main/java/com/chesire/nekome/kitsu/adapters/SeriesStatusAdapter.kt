package com.chesire.nekome.kitsu.adapters

import com.chesire.nekome.core.flags.SeriesStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val CURRENT = "current"
private const val FINISHED = "finished"
private const val TBA = "tba"
private const val UNRELEASED = "unreleased"
private const val UPCOMING = "upcoming"
private const val UNKNOWN = "unknown"

/**
 * Adapter to plug into retrofit to convert strings into [SeriesStatus] values.
 */
class SeriesStatusAdapter {
    /**
     * Converts [status] into the [SeriesStatus] value.
     */
    @FromJson
    fun seriesStatusFromString(status: String): SeriesStatus {
        return when (status) {
            CURRENT -> SeriesStatus.Current
            FINISHED -> SeriesStatus.Finished
            TBA -> SeriesStatus.TBA
            UNRELEASED -> SeriesStatus.Unreleased
            UPCOMING -> SeriesStatus.Upcoming
            else -> SeriesStatus.Unknown
        }
    }

    /**
     * Converts [seriesStatus] into its [String] value.
     */
    @ToJson
    fun seriesStatusToString(seriesStatus: SeriesStatus): String {
        return when (seriesStatus) {
            SeriesStatus.Current -> CURRENT
            SeriesStatus.Finished -> FINISHED
            SeriesStatus.TBA -> TBA
            SeriesStatus.Unreleased -> UNRELEASED
            SeriesStatus.Upcoming -> UPCOMING
            else -> UNKNOWN
        }
    }
}
