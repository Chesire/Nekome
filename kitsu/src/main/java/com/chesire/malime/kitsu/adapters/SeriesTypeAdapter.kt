package com.chesire.malime.kitsu.adapters

import com.chesire.malime.server.flags.SeriesType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ANIME = "anime"
private const val MANGA = "manga"
private const val UNKNOWN = "unknown"

class SeriesTypeAdapter {
    @FromJson
    fun seriesTypeFromString(type: String): SeriesType {
        return when (type) {
            ANIME -> SeriesType.Anime
            MANGA -> SeriesType.Manga
            else -> SeriesType.Unknown
        }
    }

    @ToJson
    fun seriesTypeToString(type: SeriesType): String {
        return when (type) {
            SeriesType.Anime -> ANIME
            SeriesType.Manga -> MANGA
            else -> UNKNOWN
        }
    }
}
