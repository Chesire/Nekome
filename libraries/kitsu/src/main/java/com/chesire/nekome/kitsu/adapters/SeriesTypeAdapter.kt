package com.chesire.nekome.kitsu.adapters

import com.chesire.nekome.seriesflags.SeriesType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

private const val ANIME = "anime"
private const val MANGA = "manga"
private const val UNKNOWN = "unknown"

/**
 * Adapter to plug into retrofit to convert strings into [SeriesType] values.
 */
class SeriesTypeAdapter {
    /**
     * Converts [type] into the [SeriesType] value.
     */
    @FromJson
    fun seriesTypeFromString(type: String): SeriesType {
        return when (type) {
            ANIME -> SeriesType.Anime
            MANGA -> SeriesType.Manga
            else -> SeriesType.Unknown
        }
    }

    /**
     * Converts [type] into its [String] value.
     */
    @ToJson
    fun seriesTypeToString(type: SeriesType): String {
        return when (type) {
            SeriesType.Anime -> ANIME
            SeriesType.Manga -> MANGA
            else -> UNKNOWN
        }
    }
}
