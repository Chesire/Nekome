package com.chesire.nekome.kitsu.auth.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Entity to use when attempting to authorize as a user.
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "grant_type")
    val grantType: String = "password"
)
