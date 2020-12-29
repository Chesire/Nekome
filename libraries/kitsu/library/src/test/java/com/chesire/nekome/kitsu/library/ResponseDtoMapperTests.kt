package com.chesire.nekome.kitsu.library

import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.UserSeriesStatus
import com.chesire.nekome.kitsu.library.dto.AddResponseDto
import com.chesire.nekome.kitsu.library.dto.DataDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResponseDtoMapperTests {

    private val map = ResponseDtoMapper()

    @Test
    fun `toLibraryDomain with no relationships, returns null`() {
        val input = AddResponseDto(
            DataDto(
                0,
                DataDto.Attributes(
                    UserSeriesStatus.Unknown,
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

        val output = map.toLibraryDomain(input)

        assertNull(output)
    }

    @Test
    fun `toLibraryDomain with no matching ids, returns null`() {
        val input = AddResponseDto(
            createDataDto(SeriesType.Anime, 15),
            listOf(
                createIncludedDto(SeriesType.Anime, 10)
            )
        )

        val output = map.toLibraryDomain(input)

        assertNull(output)
    }

    @Test
    fun `toLibraryDomain converts AddResponseDto to LibraryDomain`() {
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

        val output = map.toLibraryDomain(input)

        assertEquals(expected.id, output?.id)
        assertEquals(expected.attributes.canonicalTitle, output?.title)
    }
}
