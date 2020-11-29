package com.chesire.nekome.kitsu.user.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response DTO for interacting with the Kitsu user api.
 */
@JsonClass(generateAdapter = true)
data class UserResponseDto(
    @Json(name = "data")
    val data: List<UserItemDto>
)
