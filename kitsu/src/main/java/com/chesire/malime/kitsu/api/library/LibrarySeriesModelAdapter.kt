package com.chesire.malime.kitsu.api.library

import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.squareup.moshi.FromJson

@Suppress("unused")
class LibrarySeriesModelAdapter {
    @FromJson
    fun seriesModelFromAddResponse(response: AddResponse): SeriesModel {
        val series = response.series.first()
        val library = response.data
        return SeriesModel(
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
