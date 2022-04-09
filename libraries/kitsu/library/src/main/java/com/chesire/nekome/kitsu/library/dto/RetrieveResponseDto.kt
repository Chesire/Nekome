package com.chesire.nekome.kitsu.library.dto

import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu library retrieve endpoint.
 */
@JsonClass(generateAdapter = true)
data class RetrieveResponseDto(
    @Json(name = "data")
    val data: List<DataDto>,
    @Json(name = "included")
    val included: List<IncludedDto>?,
    @Json(name = "links")
    val links: Links
)
