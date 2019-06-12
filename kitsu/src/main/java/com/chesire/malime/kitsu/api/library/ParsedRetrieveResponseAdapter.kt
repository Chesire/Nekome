package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.squareup.moshi.FromJson

class ParsedRetrieveResponseAdapter {
    @Suppress("LongMethod")
    @FromJson
    fun parseRetrieveResponse(response: RetrieveResponse): ParsedRetrieveResponse {
        val seriesModels = response
            .data
            .mapNotNull { library ->
                val seriesId = library.relationships.anime?.data?.id
                    ?: library.relationships.manga?.data?.id

                response.series
                    .find { it.id == seriesId }
                    ?.let { series ->
                        return@mapNotNull SeriesModel(
                            id = series.id,
                            userId = library.id,
                            type = series.type,
                            subtype = series.attributes.subtype,
                            slug = series.attributes.slug,
                            title = series.attributes.canonicalTitle,
                            seriesStatus = series.attributes.status,
                            userSeriesStatus = library.attributes.status,
                            progress = library.attributes.progress,
                            totalLength = series.attributes.episodeCount
                                ?: series.attributes.chapterCount
                                ?: 0,
                            posterImage = series.attributes.posterImage ?: ImageModel.empty,
                            coverImage = series.attributes.coverImage ?: ImageModel.empty,
                            nsfw = series.attributes.nsfw,
                            startDate = series.attributes.startDate ?: "",
                            endDate = series.attributes.endDate ?: ""
                        )
                    }
            }

        return ParsedRetrieveResponse(seriesModels, response.links)
    }
}
