package com.chesire.malime.kitsu.api.search

import com.chesire.malime.server.flags.UserSeriesStatus
import com.chesire.malime.server.models.ImageModel
import com.chesire.malime.server.models.SeriesModel
import com.squareup.moshi.FromJson

class SearchSeriesModelAdapter {
    @FromJson
    fun seriesModelsFromSearchResponse(response: SearchResponse): List<SeriesModel> {
        return response.data.map {
            SeriesModel(
                id = it.id,
                userId = 0,
                type = it.type,
                subtype = it.attributes.subtype,
                slug = it.attributes.slug,
                title = it.attributes.canonicalTitle,
                seriesStatus = it.attributes.status,
                userSeriesStatus = UserSeriesStatus.Unknown,
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
