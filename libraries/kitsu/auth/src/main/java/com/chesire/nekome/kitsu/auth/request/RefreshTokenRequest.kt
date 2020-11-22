package com.chesire.nekome.kitsu.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Entity to use when requesting the auth token is refreshed.
 */
@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "grant_type")
    val grantType: String = "refresh_token"
)
