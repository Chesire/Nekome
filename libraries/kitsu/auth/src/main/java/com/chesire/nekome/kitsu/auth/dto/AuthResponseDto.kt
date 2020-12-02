package com.chesire.nekome.kitsu.auth.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for responses from the Kitsu auth endpoints.
 */
@JsonClass(generateAdapter = true)
data class AuthResponseDto(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "refresh_token")
    val refreshToken: String
)
