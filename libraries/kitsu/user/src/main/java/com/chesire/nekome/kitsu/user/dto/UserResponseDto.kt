package com.chesire.nekome.kitsu.user.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for responses from the Kitsu user endpoint.
 */
@JsonClass(generateAdapter = true)
data class UserResponseDto(
    @Json(name = "data")
    val data: List<UserItemDto>
)
