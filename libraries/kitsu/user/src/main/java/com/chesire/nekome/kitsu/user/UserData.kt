package com.chesire.nekome.kitsu.user

import com.chesire.nekome.kitsu.user.KitsuUserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "data")
    val data: List<KitsuUserEntity>
)
