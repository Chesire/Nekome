package com.chesire.malime.kitsu.api.library

import com.chesire.malime.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddResponse(
    @Json(name = "data")
    val data: LibraryEntry,
    @Json(name = "included")
    val series: List<SeriesItem>
)
