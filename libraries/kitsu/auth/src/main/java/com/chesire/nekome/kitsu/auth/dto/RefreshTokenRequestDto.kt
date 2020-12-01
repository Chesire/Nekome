package com.chesire.nekome.kitsu.auth.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO to use when requesting to refresh the auth token.
 */
@JsonClass(generateAdapter = true)
data class RefreshTokenRequestDto(
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "grant_type")
    val grantType: String = "refresh_token"
)
