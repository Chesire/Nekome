package com.chesire.nekome.kitsu.search.dto

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.Subtype
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu search endpoint.
 */
@JsonClass(generateAdapter = true)
data class SearchItemDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "type")
    val type: SeriesType,
    @Json(name = "attributes")
    val attributes: Attributes
) {
    /**
     * Attributes of the search item.
     */
    @JsonClass(generateAdapter = true)
    data class Attributes(
        @Json(name = "synopsis")
        val synopsis: String,
        @Json(name = "canonicalTitle")
        val canonicalTitle: String,
        @Json(name = "subtype")
        val subtype: Subtype,
        @Json(name = "posterImage")
        val posterImage: ImageModel?
    )
}
