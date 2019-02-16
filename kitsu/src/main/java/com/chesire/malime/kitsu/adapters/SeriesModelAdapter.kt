package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.search.SearchAnimeResponse
import com.squareup.moshi.FromJson

@Suppress("unused")
class SeriesModelAdapter {
    @FromJson
    fun seriesModelsFromSearchAnimeResponse(response: SearchAnimeResponse): List<SeriesModel> {
        return response.data.map {
            SeriesModel(
                id = it.id,
                type = it.type,
                // sub type
                slug = it.attributes.slug,
                title = it.attributes.canonicalTitle,
                // series status
                progress = it.attributes.progress,
                totalLength = it.attributes.episodeCount,
                posterImage = it.attributes.posterImage,
                coverImage = it.attributes.coverImage,
                nsfw = it.attributes.nsfw,
                startDate = it.attributes.startedAt,
                endDate = it.attributes.finishedAt
            )
        }
    }
}
