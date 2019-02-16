package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "data")
    val data: Array<SearchData>
) {
    @JsonClass(generateAdapter = true)
    data class SearchData(
        @Json(name = "id")
        val id: Int,
        @Json(name = "type")
        val type: SeriesType,
        @Json(name = "attributes")
        val attributes: SearchAttributes
    ) {
        @JsonClass(generateAdapter = true)
        data class SearchAttributes(
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
            @Json(name = "coverImage")
            val coverImage: ImageModel?,
            @Json(name = "chapterCount")
            val chapterCount: Int?,
            @Json(name = "episodeCount")
            val episodeCount: Int?,
            @Json(name = "nsfw")
            val nsfw: Boolean = false
        )
    }
}
