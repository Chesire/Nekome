package com.chesire.nekome.kitsu.api.trending

import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class to parse a response from [KitsuTrendingService] into an object.
 */
@JsonClass(generateAdapter = true)
data class TrendingResponse(
    @Json(name = "data")
    val data: List<SeriesItem>
)
