package com.chesire.nekome.library

import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.entity.SeriesEntity
import com.chesire.nekome.dataflags.SeriesStatus
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.Subtype
import com.chesire.nekome.dataflags.UserSeriesStatus
import com.chesire.nekome.library.api.LibraryDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryDomainMapperTests {

    private val map = LibraryDomainMapper()

    @Test
    fun `toSeriesEntity converts LibraryDomain to SeriesEntity`() {
        val input = LibraryDomain(
            15,
            20,
            SeriesType.Manga,
            Subtype.Manhwa,
            "slug",
            "title",
            SeriesStatus.Upcoming,
            UserSeriesStatus.Planned,
            0,
            12,
            ImageModel.empty,
            "startDate",
            "endDate"
        )

        val output = map.toSeriesEntity(input)

        assertEquals(input.id, output.id)
        assertEquals(input.userId, output.userId)
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.slug, output.slug)
        assertEquals(input.title, output.title)
        assertEquals(input.seriesStatus, output.seriesStatus)
        assertEquals(input.userSeriesStatus, output.userSeriesStatus)
        assertEquals(input.progress, output.progress)
        assertEquals(input.totalLength, output.totalLength)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }

    @Test
    fun `toSeriesEntity converts SeriesDomain to SeriesEntity`() {
        val input = SeriesDomain(
            15,
            20,
            SeriesType.Manga,
            Subtype.Manhwa,
            "slug",
            "title",
            SeriesStatus.Upcoming,
            UserSeriesStatus.Planned,
            0,
            12,
            ImageModel.empty,
            "startDate",
            "endDate"
        )

        val output = map.toSeriesEntity(input)

        assertEquals(input.id, output.id)
        assertEquals(input.userId, output.userId)
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.slug, output.slug)
        assertEquals(input.canonicalTitle, output.title)
        assertEquals(input.seriesStatus, output.seriesStatus)
        assertEquals(input.userSeriesStatus, output.userSeriesStatus)
        assertEquals(input.progress, output.progress)
        assertEquals(input.totalLength, output.totalLength)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }

    @Test
    fun `toSeriesDomain converts SeriesEntity to SeriesDomain`() {
        val input = SeriesEntity(
            15,
            20,
            SeriesType.Manga,
            Subtype.Manhwa,
            "slug",
            "title",
            SeriesStatus.Upcoming,
            UserSeriesStatus.Planned,
            0,
            12,
            ImageModel.empty,
            "startDate",
            "endDate"
        )

        val output = map.toSeriesDomain(input)

        assertEquals(input.id, output.id)
        assertEquals(input.userId, output.userId)
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.slug, output.slug)
        assertEquals(input.title, output.canonicalTitle)
        assertEquals(input.seriesStatus, output.seriesStatus)
        assertEquals(input.userSeriesStatus, output.userSeriesStatus)
        assertEquals(input.progress, output.progress)
        assertEquals(input.totalLength, output.totalLength)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }
}
