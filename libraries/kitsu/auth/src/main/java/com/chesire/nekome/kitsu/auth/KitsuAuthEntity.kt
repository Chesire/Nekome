package com.chesire.nekome.kitsu.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KitsuAuthEntity(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "created_at")
    val createdAt: Long,
    @Json(name = "expires_in")
    val expiresIn: Long,
    @Json(name = "refresh_token")
    val refreshToken: String,
    @Json(name = "scope")
    val scope: String,
    @Json(name = "token_type")
    val tokenType: String
)
