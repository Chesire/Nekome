package com.chesire.nekome.kitsu.search.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for responses from the Kitsu search endpoint.
 */
@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "data")
    val data: List<SearchItemDto>
)
