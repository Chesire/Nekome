package com.chesire.nekome.kitsu.library.dto

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for the "data" part of the library api, contains the user information about a series item.
 */
@JsonClass(generateAdapter = true)
data class DataDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: Attributes,
    @Json(name = "relationships")
    val relationships: Relationships
) {
    /**
     * Information of the users details about a series.
     */
    @JsonClass(generateAdapter = true)
    data class Attributes(
        @Json(name = "status")
        val status: UserSeriesStatus,
        @Json(name = "progress")
        val progress: Int,
        @Json(name = "volumesOwned")
        val volumesOwned: Int?,
        @Json(name = "ratingTwenty")
        val rating: Int?,
        @Json(name = "startedAt")
        val startedAt: String?,
        @Json(name = "finishedAt")
        val finishedAt: String?
    )

    /**
     * Information about the anime/manga relationship mappings.
     */
    @JsonClass(generateAdapter = true)
    data class Relationships(
        @Json(name = "anime")
        val anime: RelationshipObject? = null,
        @Json(name = "manga")
        val manga: RelationshipObject? = null
    ) {
        /**
         * Data container for relationship data.
         */
        @JsonClass(generateAdapter = true)
        data class RelationshipObject(
            @Json(name = "data")
            val data: RelationshipData? = null
        ) {
            /**
             * Contains the information for the [type] and [id] of a mapping.
             */
            @JsonClass(generateAdapter = true)
            data class RelationshipData(
                @Json(name = "id")
                val id: Int
            )
        }
    }
}
