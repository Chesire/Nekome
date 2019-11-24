package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "data")
    val data: List<SeriesItem>,
    @Json(name = "links")
    val links: Links
)
