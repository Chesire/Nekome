package com.chesire.malime.kitsu.api.library

import com.chesire.malime.kitsu.api.intermediaries.SeriesItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LibraryResponse(
    @Json(name = "data")
    val data: List<LibraryEntry>,
    @Json(name = "included")
    val series: List<SeriesItem>
) {
    @JsonClass(generateAdapter = true)
    data class LibraryEntry(
        @Json(name = "id")
        val id: Int, // represents the id in the users library
        @Json(name = "attributes")
        val attributes: LibraryAttributes,
        @Json(name = "relationships")
        val relationships: LibraryRelationships
    ) {
        @JsonClass(generateAdapter = true)
        data class LibraryAttributes(
            @Json(name = "status")
            val status: String,
            @Json(name = "progress")
            val progress: Int,
            @Json(name = "startedAt")
            val startedAt: String?,
            @Json(name = "finishedAt")
            val finishedAt: String?
        )

        @JsonClass(generateAdapter = true)
        data class LibraryRelationships(
            @Json(name = "anime")
            val anime: RelationshipObject? = null,
            @Json(name = "manga")
            val manga: RelationshipObject? = null
        ) {
            @JsonClass(generateAdapter = true)
            data class RelationshipObject(
                @Json(name = "data")
                val data: RelationshipData
            ) {
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
}
