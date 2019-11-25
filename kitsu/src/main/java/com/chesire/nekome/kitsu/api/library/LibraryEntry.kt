package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Class to parse a response from [KitsuLibraryService] into an object.
 */
@JsonClass(generateAdapter = true)
data class LibraryEntry(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: LibraryAttributes,
    @Json(name = "relationships")
    val relationships: LibraryRelationships
) {
    /**
     * Class to parse a response from [KitsuLibraryService] into an object.
     */
    @JsonClass(generateAdapter = true)
    data class LibraryAttributes(
        @Json(name = "status")
        val status: UserSeriesStatus,
        @Json(name = "progress")
        val progress: Int,
        @Json(name = "startedAt")
        val startedAt: String?,
        @Json(name = "finishedAt")
        val finishedAt: String?
    )

    /**
     * Class to parse a response from [KitsuLibraryService] into an object.
     */
    @JsonClass(generateAdapter = true)
    data class LibraryRelationships(
        @Json(name = "anime")
        val anime: RelationshipObject? = null,
        @Json(name = "manga")
        val manga: RelationshipObject? = null
    ) {
        /**
         * Class to parse a response from [KitsuLibraryService] into an object.
         */
        @JsonClass(generateAdapter = true)
        data class RelationshipObject(
            @Json(name = "data")
            val data: RelationshipData? = null
        ) {
            /**
             * Class to parse a response from [KitsuLibraryService] into an object.
             */
            @JsonClass(generateAdapter = true)
            data class RelationshipData(
                @Json(name = "type")
                val type: String,
                @Json(name = "id")
                val id: Int
            )
        }
    }
}
