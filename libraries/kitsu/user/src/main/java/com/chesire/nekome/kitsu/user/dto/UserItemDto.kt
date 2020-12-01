package com.chesire.nekome.kitsu.user.dto

import com.chesire.nekome.core.flags.RatingSystem
import com.chesire.nekome.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO from the Kitsu user endpoint.
 */
@JsonClass(generateAdapter = true)
data class UserItemDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: Attributes
) {
    /**
     * Attributes of the user item.
     */
    @JsonClass(generateAdapter = true)
    data class Attributes(
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
