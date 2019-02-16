package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.models.ImageModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchAnimeResponse(
    @Json(name = "data")
    val data: Array<SearchAnimeData>
) {
    @JsonClass(generateAdapter = true)
    data class SearchAnimeData(
        @Json(name = "id")
        val id: Int,
        @Json(name = "type")
        val type: SeriesType,
        @Json(name = "attributes")
        val attributes: AnimeDetailsAttributes
    ) {
        @JsonClass(generateAdapter = true)
        data class AnimeDetailsAttributes(
            @Json(name = "slug")
            val slug: String,
            @Json(name = "canonicalTitle")
            val canonicalTitle: String,
            @Json(name = "status")
            val status: String,
            @Json(name = "progress")
            val progress: Int,
            @Json(name = "subtype")
            val subtype: Subtype,
            @Json(name = "posterImage")
            val posterImage: ImageModel,
            @Json(name = "coverImage")
            val coverImage: ImageModel,
            @Json(name = "episodeCount")
            val episodeCount: Int,
            @Json(name = "chapterCount")
            val chapterCount: Int,
            @Json(name = "nsfw")
            val nsfw: Boolean,
            @Json(name = "startedAt")
            val startedAt: String = "",
            @Json(name = "finishedAt")
            val finishedAt: String = ""
        )
    }
}
