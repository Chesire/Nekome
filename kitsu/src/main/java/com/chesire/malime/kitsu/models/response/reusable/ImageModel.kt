package com.chesire.malime.kitsu.models.response.reusable

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageModel(
    @Json(name = "tiny")
    val tiny: String = "",
    @Json(name = "small")
    val small: String = "",
    @Json(name = "medium")
    val medium: String = "",
    @Json(name = "large")
    val large: String = "",
    @Json(name = "original")
    val original: String = ""
)
