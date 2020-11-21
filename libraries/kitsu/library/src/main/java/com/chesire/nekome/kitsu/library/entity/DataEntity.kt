package com.chesire.nekome.kitsu.library.entity

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataEntity(
    @Json(name = "id")
    val id: Int,
    @Json(name = "attributes")
    val attributes: Attributes,
    @Json(name = "relationships")
    val relationships: Relationships
) {
    @JsonClass(generateAdapter = true)
    data class Attributes(
        @Json(name = "status")
        val status: UserSeriesStatus,
        @Json(name = "progress")
        val progress: Int,
        @Json(name = "startedAt")
        val startedAt: String?,
        @Json(name = "finishedAt")
        val finishedAt: String?
    )

    @JsonClass(generateAdapter = true)
    data class Relationships(
        @Json(name = "anime")
        val anime: RelationshipObject? = null,
        @Json(name = "manga")
        val manga: RelationshipObject? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class RelationshipObject(
            @Json(name = "data")
            val data: RelationshipData? = null
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
