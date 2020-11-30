package com.chesire.nekome.library

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.library.api.LibraryDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class LibraryDomainMapperTests {

    private val map = LibraryDomainMapper()

    @Test
    fun `toSeriesModel converts LibraryDomain to SeriesModel`() {
        val input = LibraryDomain(
            15,
            20,
            SeriesType.Manga,
            Subtype.Manhwa,
            "slug",
            "synopsis",
            "title",
            SeriesStatus.Upcoming,
            UserSeriesStatus.Planned,
            0,
            12,
            ImageModel.empty,
            ImageModel.empty,
            false,
            "startDate",
            "endDate"
        )

        val output = map.toSeriesModel(input)

        assertEquals(input.id, output.id)
        assertEquals(input.userId, output.userId)
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.slug, output.slug)
        assertEquals(input.synopsis, output.synopsis)
        assertEquals(input.title, output.title)
        assertEquals(input.seriesStatus, output.seriesStatus)
        assertEquals(input.userSeriesStatus, output.userSeriesStatus)
        assertEquals(input.progress, output.progress)
        assertEquals(input.totalLength, output.totalLength)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.coverImage, output.coverImage)
        assertEquals(input.nsfw, output.nsfw)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }
}
