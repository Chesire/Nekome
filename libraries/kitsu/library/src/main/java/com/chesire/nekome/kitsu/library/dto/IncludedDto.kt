package com.chesire.nekome.kitsu.library.dto

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.dataflags.SeriesStatus
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.Subtype
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for the "included" part of the library api, represents the information about a series.
 */
@JsonClass(generateAdapter = true)
data class IncludedDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "type")
    val type: SeriesType,
    @Json(name = "attributes")
    val attributes: Attributes
) {
    /**
     * Information about the attributes of a series.
     */
    @JsonClass(generateAdapter = true)
    data class Attributes(
        @Json(name = "slug")
        val slug: String,
        @Json(name = "canonicalTitle")
        val canonicalTitle: String,
        @Json(name = "startDate")
        val startDate: String?,
        @Json(name = "endDate")
        val endDate: String?,
        @Json(name = "subtype")
        val subtype: Subtype,
        @Json(name = "status")
        val status: SeriesStatus,
        @Json(name = "posterImage")
        val posterImage: ImageModel?,
        @Json(name = "chapterCount")
        val chapterCount: Int?,
        @Json(name = "episodeCount")
        val episodeCount: Int?
    )
}
