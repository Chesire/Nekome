package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.intermediaries.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParsedLibraryResponse(
    @Json(name = "series")
    val series: List<SeriesModel>,
    @Json(name = "links")
    val links: Links
)
