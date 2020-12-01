package com.chesire.nekome.kitsu.library.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu library add/update endpoint.
 */
@JsonClass(generateAdapter = true)
data class AddResponseDto(
    @Json(name = "data")
    val data: DataDto,
    @Json(name = "included")
    val included: List<IncludedDto>
)
