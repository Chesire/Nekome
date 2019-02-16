package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.SeriesType
import com.squareup.moshi.FromJson

private const val ANIME = "anime"
private const val MANGA = "manga"

@Suppress("unused")
class SeriesTypeAdapter {
    @FromJson
    fun seriesTypeFromString(type: String): SeriesType {
        return when (type) {
            ANIME -> SeriesType.Anime
            MANGA -> SeriesType.Manga
            else -> SeriesType.Unknown
        }
    }
}
