package com.chesire.nekome.app.series.collection.ui

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.series.SeriesDomain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DomainMapperTest {

    private lateinit var mapper: DomainMapper
    private val initialDomain = SeriesDomain(
        id = 0,
        userId = 1,
        type = SeriesType.Anime,
        subtype = Subtype.Movie,
        slug = "anime-series",
        title = "Anime series",
        seriesStatus = SeriesStatus.Unreleased,
        userSeriesStatus = UserSeriesStatus.Planned,
        progress = 1,
        totalLength = 1,
        rating = 3,
        posterImage = ImageModel(
            tiny = ImageModel.ImageData(
                url = "tiny",
                width = 0,
                height = 0
            ),
            small = ImageModel.ImageData(
                url = "small",
                width = 0,
                height = 0
            ),
            medium = ImageModel.ImageData(
                url = "medium",
                width = 0,
                height = 0
            ),
            large = ImageModel.ImageData(
                url = "large",
                width = 0,
                height = 0
            )
        ),
        startDate = "abc",
        endDate = "def"
    )

    @Before
    fun setup() {
        mapper = DomainMapper()
    }

    @Test
    fun `When toSeries, Then list of Series item is returned`() {
        val input = listOf(initialDomain)
        val expected = listOf(
            Series(
                userId = 1,
                title = "Anime series",
                posterImageUrl = "tiny",
                subtype = Subtype.Movie.name,
                progress = "1 / 1",
                startDate = "abc",
                endDate = "def",
                rating = 3,
                showPlusOne = false,
                isUpdating = false
            )
        )

        val result = mapper.toSeries(input)

        assertEquals(expected, result)
    }

    @Test
    fun `Given no known series length, When toSeries, Then max length is set to -`() {
        val input = listOf(initialDomain.copy(totalLength = 0))
        val expected = listOf(
            Series(
                userId = 1,
                title = "Anime series",
                posterImageUrl = "tiny",
                subtype = Subtype.Movie.name,
                progress = "1 / -",
                startDate = "abc",
                endDate = "def",
                rating = 3,
                showPlusOne = true,
                isUpdating = false
            )
        )

        val result = mapper.toSeries(input)

        assertEquals(expected, result)
    }

    @Test
    fun `Given user can +1 progress, When toSeries, Then shouldPlusOne is true`() {
        val input = listOf(initialDomain.copy(totalLength = 15))
        val expected = listOf(
            Series(
                userId = 1,
                title = "Anime series",
                posterImageUrl = "tiny",
                subtype = Subtype.Movie.name,
                progress = "1 / 15",
                startDate = "abc",
                endDate = "def",
                rating = 3,
                showPlusOne = true,
                isUpdating = false
            )
        )

        val result = mapper.toSeries(input)

        assertEquals(expected, result)
    }
}
