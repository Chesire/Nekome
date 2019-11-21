package com.chesire.malime.kitsu.api.intermediaries

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "first")
    val first: String = "",
    @Json(name = "next")
    val next: String = "",
    @Json(name = "last")
    val last: String = ""
)
