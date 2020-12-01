package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.trending.dto.TrendingItemDto
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class TrendingItemDtoMapperTests {

    private val map = TrendingItemDtoMapper()

    @Test
    fun `toSearchDomain converts SearchItemDto to SearchDomain`() {
        val posterImageInput = createImageModel(
            ImageModel.ImageData("posterTiny", 10, 5),
            ImageModel.ImageData("posterSmall", 20, 10),
            ImageModel.ImageData("posterMedium", 30, 15),
            ImageModel.ImageData("posterLarge", 40, 20),
        )
        val coverImageInput = createImageModel(
            ImageModel.ImageData("coverTiny", 5, 10),
            ImageModel.ImageData("coverSmall", 10, 20),
            ImageModel.ImageData("coverMedium", 15, 30),
            ImageModel.ImageData("coverLarge", 20, 40),
        )
        val input = TrendingItemDto(
            10,
            SeriesType.Anime,
            TrendingItemDto.Attributes(
                "slug",
                "synopsis",
                "canonicalTitle",
                "startDate",
                "endDate",
                Subtype.Movie,
                SeriesStatus.Upcoming,
                posterImageInput,
                coverImageInput,
                0,
                11,
                true
            )
        )

        val output = map.toTrendingDomain(input)

        assertEquals(input.id, output.id)
        assertEquals(input.type, output.type)
        assertEquals(input.attributes.slug, output.slug)
        assertEquals(input.attributes.synopsis, output.synopsis)
        assertEquals(input.attributes.canonicalTitle, output.canonicalTitle)
        assertEquals("", output.averageRating)
        assertEquals(input.attributes.startDate, output.startDate)
        assertEquals(input.attributes.endDate, output.endDate)
        assertEquals(input.attributes.subtype, output.subtype)
        assertEquals(input.attributes.status, output.status)
        assertEquals(input.attributes.posterImage, output.posterImage)
        assertEquals(input.attributes.coverImage, output.coverImage)
        assertEquals(input.attributes.chapterCount, output.chapterCount)
        assertEquals(input.attributes.episodeCount, output.episodeCount)
        assertEquals(input.attributes.nsfw, output.nsfw)
    }
}
