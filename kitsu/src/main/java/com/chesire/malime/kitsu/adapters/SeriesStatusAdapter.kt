package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.SeriesStatus
import com.squareup.moshi.FromJson

private const val CURRENT = "current"
private const val FINISHED = "finished"
private const val TBA = "tba"
private const val UNRELEASED = "unreleased"
private const val UPCOMING = "upcoming"

@Suppress("unused")
class SeriesStatusAdapter {
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
}
