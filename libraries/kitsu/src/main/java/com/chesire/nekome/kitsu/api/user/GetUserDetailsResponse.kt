package com.chesire.nekome.kitsu.api.user

import com.chesire.nekome.core.flags.RatingSystem
import com.chesire.nekome.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class to parse a response from [KitsuUserService] into an object.
 */
@JsonClass(generateAdapter = true)
data class GetUserDetailsResponse(
    @Json(name = "data")
    val data: List<UserDetailsData>
) {
    /**
     * Class to parse a response from [KitsuUserService] into an object.
     */
    @JsonClass(generateAdapter = true)
    data class UserDetailsData(
        @Json(name = "id")
        val id: Int,
        @Json(name = "attributes")
        val attributes: UserDetailsAttributes
    ) {
        /**
         * Class to parse a response from [KitsuUserService] into an object.
         */
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
