package com.chesire.nekome.kitsu.search.dto

import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for responses from the Kitsu search endpoint.
 */
@JsonClass(generateAdapter = true)
data class SearchResponseDto(
    @Json(name = "data")
    val data: List<SearchItemDto>,
    @Json(name = "links")
    val links: Links
)
