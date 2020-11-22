package com.chesire.nekome.kitsu.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Response model for interacting with the Kitsu user api.
 */
@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "data")
    val data: List<KitsuUserEntity>
)
