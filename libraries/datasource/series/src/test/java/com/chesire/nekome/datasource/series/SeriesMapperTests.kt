package com.chesire.nekome.datasource.series

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.database.entity.SeriesEntity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SeriesMapperTests {

    private lateinit var map: SeriesMapper

    @Before
    fun setup() {
        map = SeriesMapper()
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
            0,
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
        assertEquals(input.rating, output.rating)
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
            0,
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
        assertEquals(input.title, output.title)
        assertEquals(input.seriesStatus, output.seriesStatus)
        assertEquals(input.userSeriesStatus, output.userSeriesStatus)
        assertEquals(input.progress, output.progress)
        assertEquals(input.totalLength, output.totalLength)
        assertEquals(input.rating, output.rating)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }
}
