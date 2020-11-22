package com.chesire.nekome.kitsu.library.entity

import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Entity from the Kitsu library retrieve endpoint.
 */
@JsonClass(generateAdapter = true)
data class KitsuLibraryEntities(
    @Json(name = "data")
    val data: List<DataEntity>,
    @Json(name = "included")
    val included: List<IncludedEntity>,
    @Json(name = "links")
    val links: Links
)
