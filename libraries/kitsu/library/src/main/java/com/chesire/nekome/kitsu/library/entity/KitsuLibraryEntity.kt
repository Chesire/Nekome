package com.chesire.nekome.kitsu.library.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KitsuLibraryEntity(
    @Json(name = "data")
    val data: DataEntity,
    @Json(name = "included")
    val included: List<IncludedEntity>
)
