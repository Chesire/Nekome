package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RetrieveResponse(
    @Json(name = "data")
    val data: List<LibraryEntry>,
    @Json(name = "included")
    val series: List<SeriesItem>,
    @Json(name = "links")
    val links: Links
)
