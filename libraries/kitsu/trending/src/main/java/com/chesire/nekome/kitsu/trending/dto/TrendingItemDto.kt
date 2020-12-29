package com.chesire.nekome.kitsu.trending.dto

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.dataflags.SeriesType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu trending endpoint.
 */
@JsonClass(generateAdapter = true)
data class TrendingItemDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "type")
    val type: SeriesType,
    @Json(name = "attributes")
    val attributes: Attributes
) {
    /**
     * Attributes of the trending item.
     */
    @JsonClass(generateAdapter = true)
    data class Attributes(
        @Json(name = "canonicalTitle")
        val canonicalTitle: String,
        @Json(name = "posterImage")
        val posterImage: ImageModel?
    )
}
