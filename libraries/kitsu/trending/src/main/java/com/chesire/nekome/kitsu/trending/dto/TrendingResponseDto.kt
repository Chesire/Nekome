package com.chesire.nekome.kitsu.trending.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for responses from the Kitsu trending endpoint.
 */
@JsonClass(generateAdapter = true)
data class TrendingResponseDto(
    @Json(name = "data")
    val data: List<TrendingItemDto>
)
