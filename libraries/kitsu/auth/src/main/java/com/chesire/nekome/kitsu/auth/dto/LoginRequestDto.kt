package com.chesire.nekome.kitsu.auth.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO to use when attempting to authorize as a user.
 */
@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "grant_type")
    val grantType: String = "password"
)
