package com.chesire.nekome.kitsu.trending.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class to parse a response from [KitsuTrendingService] into an object.
 */
@JsonClass(generateAdapter = true)
data class TrendingResponseDto(
    @Json(name = "data")
    val data: List<TrendingItemDto>
)
