package com.chesire.nekome.kitsu.library

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DataDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ResponseDtoMapperTests {

    private lateinit var map: ResponseDtoMapper

    @Before
    fun setup() {
        map = ResponseDtoMapper()
    }

    @Test
    fun `toSeriesDomain with no relationships, returns null`() {
        val input = AddResponseDto(
            DataDto(
                0,
                DataDto.Attributes(
                    UserSeriesStatus.Unknown,
                    0,
                    0,
                    "startedAt",
                    "finishedAt"
                ),
                DataDto.Relationships()
            ),
            listOf(
                createIncludedDto(SeriesType.Anime)
            )
        )

        val output = map.toSeriesDomain(input)

        assertNull(output)
    }

    @Test
    fun `toSeriesDomain with no matching ids, returns null`() {
        val input = AddResponseDto(
            createDataDto(SeriesType.Anime, 15),
            listOf(
                createIncludedDto(SeriesType.Anime, 10)
            )
        )

        val output = map.toSeriesDomain(input)

        assertNull(output)
    }

    @Test
    fun `toSeriesDomain converts AddResponseDto to LibraryDomain`() {
        val expected = createIncludedDto(
            SeriesType.Anime,
            15,
            canonicalTitle = "Series 3"
        )
        val input = AddResponseDto(
            createDataDto(
                SeriesType.Anime,
                15
            ),
            listOf(
                createIncludedDto(
                    SeriesType.Anime,
                    11,
                    canonicalTitle = "Series 1"
                ),
                createIncludedDto(
                    SeriesType.Anime,
                    13,
                    canonicalTitle = "Series 2"
                ),
                expected,
            )
        )

        val output = map.toSeriesDomain(input)

        assertEquals(expected.id, output?.id)
        assertEquals(expected.attributes.canonicalTitle, output?.title)
    }
}
