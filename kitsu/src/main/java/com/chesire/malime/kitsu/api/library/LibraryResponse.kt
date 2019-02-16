package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class LibraryResponse(
    @Json(name = "data")
    val data: Array<LibraryEntry>,
    @Json(name = "included")
    val series: Array<SeriesItem>
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

    @JsonClass(generateAdapter = true)
    data class SeriesItem(
        @Json(name = "id")
        val id: Int,
        @Json(name = "type")
        val type: SeriesType,
        @Json(name = "attributes")
        val attributes: SeriesAttributes
    ) {
        @JsonClass(generateAdapter = true)
        data class SeriesAttributes(
            @Json(name = "slug")
            val slug: String,
            @Json(name = "canonicalTitle")
            val canonicalTitle: String,
            @Json(name = "startDate")
            val startDate: String?,
            @Json(name = "endDate")
            val endDate: String?,
            @Json(name = "subtype")
            val subtype: Subtype,
            @Json(name = "status")
            val status: SeriesStatus,
            @Json(name = "posterImage")
            val posterImage: ImageModel?,
            @Json(name = "coverImage")
            val coverImage: ImageModel?,
            @Json(name = "chapterCount")
            val chapterCount: Int?,
            @Json(name = "episodeCount")
            val episodeCount: Int?,
            @Json(name = "nsfw")
            val nsfw: Boolean = false
        )
    }
}
