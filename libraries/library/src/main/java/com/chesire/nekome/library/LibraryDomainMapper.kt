package com.chesire.nekome.library

import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.library.api.LibraryDomain
import javax.inject.Inject

/**
 * Provides ability to map between object instances for the Library.
 */
class LibraryDomainMapper @Inject constructor() {

    /**
     * Converts an instance of [LibraryDomain] into a [SeriesEntity].
     */
    fun toSeriesEntity(input: LibraryDomain) =
        SeriesEntity(
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
