package com.chesire.nekome.kitsu.api.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request object for logging in.
 */
@JsonClass(generateAdapter = true)
data class LoginRequest(
    /**
     * Username for the user, although the api expects the users email address.
     */
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "grant_type")
    val grantType: String = "password"
)
