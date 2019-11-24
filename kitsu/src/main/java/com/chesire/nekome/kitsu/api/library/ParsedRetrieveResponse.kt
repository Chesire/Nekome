package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParsedRetrieveResponse(
    @Json(name = "series")
    val series: List<SeriesModel>,
    @Json(name = "links")
    val links: Links
)
