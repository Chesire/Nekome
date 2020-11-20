package com.chesire.nekome.kitsu.user

import com.chesire.nekome.core.flags.RatingSystem
import com.chesire.nekome.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KitsuUserEntity(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: EntityAttributes
) {
    @JsonClass(generateAdapter = true)
    data class EntityAttributes(
        @Json(name = "name")
        val name: String,
        @Json(name = "slug")
        val slug: String?,
        @Json(name = "ratingSystem")
        val ratingSystem: RatingSystem,
        @Json(name = "avatar")
        val avatar: ImageModel?,
        @Json(name = "coverImage")
        val coverImage: ImageModel?
    )
}
