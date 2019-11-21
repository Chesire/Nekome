package com.chesire.malime.kitsu.extensions

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.ImageModel
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.kitsu.api.intermediaries.SeriesItem

/**
 * Converts a [List] of [SeriesItem] into a comparable [List] of [SeriesModel].
 */
internal fun List<SeriesItem>.convertToSeriesModels() = map {
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
