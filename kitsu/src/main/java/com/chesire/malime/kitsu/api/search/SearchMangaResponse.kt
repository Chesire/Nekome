package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.flags.SeriesType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMangaResponse(
    @Json(name = "data")
    val data: Array<SearchMangaData>
) {
    @JsonClass(generateAdapter = true)
    data class SearchMangaData(
        @Json(name = "id")
        val id: Int,
        @Json(name = "type")
        val type: SeriesType,
        @Json(name = "attributes")
        val attributes: MangaDetailsAttributes
    ) {
        @JsonClass(generateAdapter = true)
        data class MangaDetailsAttributes(
            @Json(name = "name")
            val name: String
        )
    }
}
