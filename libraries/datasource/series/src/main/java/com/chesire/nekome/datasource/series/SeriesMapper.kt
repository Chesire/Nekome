package com.chesire.nekome.datasource.series

import com.chesire.nekome.database.entity.SeriesEntity
import javax.inject.Inject

/**
 * Provides ability to map between object instances for the Library.
 */
class SeriesMapper @Inject constructor() {

    /**
     * Converts an instance of [SeriesDomain] into a [SeriesEntity].
     */
    fun toSeriesEntity(input: SeriesDomain) =
        SeriesEntity(
            input.id,
            input.userId,
            input.type,
            input.subtype,
            input.slug,
            input.canonicalTitle,
            input.seriesStatus,
            input.userSeriesStatus,
            input.progress,
            input.totalLength,
            input.rating,
            input.posterImage,
            input.startDate,
            input.endDate
        )

    /**
     * Converts an instance of [SeriesEntity] into a [SeriesDomain].
     */
    fun toSeriesDomain(input: SeriesEntity) =
        SeriesDomain(
            input.id,
            input.userId,
            input.type,
            input.subtype,
            input.slug,
            input.title,
            input.seriesStatus,
            input.userSeriesStatus,
            input.progress,
            input.totalLength,
            input.rating,
            input.posterImage,
            input.startDate,
            input.endDate
        )
}
