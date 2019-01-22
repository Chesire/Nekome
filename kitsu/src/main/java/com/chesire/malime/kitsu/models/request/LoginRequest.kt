package com.chesire.malime.kitsu.models.request

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "username")
    val username: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "grant_type")
    val grantType: String = "password"
)
