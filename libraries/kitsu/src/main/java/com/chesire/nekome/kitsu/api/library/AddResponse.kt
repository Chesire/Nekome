package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response object from an add request.
 */
@JsonClass(generateAdapter = true)
data class AddResponse(
    @Json(name = "data")
    val data: LibraryEntry,
    @Json(name = "included")
    val series: List<SeriesItem>
)
