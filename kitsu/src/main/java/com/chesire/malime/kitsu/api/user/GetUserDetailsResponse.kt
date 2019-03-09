package com.chesire.malime.kitsu.api.user

import com.chesire.malime.core.flags.RatingSystem
import com.chesire.malime.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetUserDetailsResponse(
    @Json(name = "data")
    val data: List<UserDetailsData>
) {
    @JsonClass(generateAdapter = true)
    data class UserDetailsData(
        @Json(name = "id")
        val id: Int,
        @Json(name = "attributes")
        val attributes: UserDetailsAttributes
    ) {
        @JsonClass(generateAdapter = true)
        data class UserDetailsAttributes(
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
}
