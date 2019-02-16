package com.chesire.malime.kitsu.api.search

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.squareup.moshi.FromJson

@Suppress("unused")
class SearchSeriesModelAdapter {
    @FromJson
    fun seriesModelsFromSearchResponse(response: SearchResponse): List<SeriesModel> {
        return response.data.map {
            SeriesModel(
                id = it.id,
                type = it.type,
                subtype = it.attributes.subtype,
                slug = it.attributes.slug,
                title = it.attributes.canonicalTitle,
                seriesStatus = it.attributes.status,
                progress = 0,
                totalLength = it.attributes.episodeCount ?: it.attributes.chapterCount ?: 0,
                posterImage = it.attributes.posterImage ?: ImageModel.empty,
                coverImage = it.attributes.coverImage ?: ImageModel.empty,
                nsfw = it.attributes.nsfw,
                startDate = it.attributes.startDate ?: "",
                endDate = it.attributes.endDate ?: ""
            )
        }
    }
}
