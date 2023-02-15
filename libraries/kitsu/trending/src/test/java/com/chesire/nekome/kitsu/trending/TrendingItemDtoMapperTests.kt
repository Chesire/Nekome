package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.flags.SeriesType
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
            ImageModel.ImageData("posterLarge", 40, 20)
        )
        val input = TrendingItemDto(
            10,
            SeriesType.Anime,
            TrendingItemDto.Attributes(
                "canonicalTitle",
                posterImageInput
            )
        )

        val output = map.toTrendingDomain(input)

        assertEquals(input.id, output.id)
        assertEquals(input.type, output.type)
        assertEquals(input.attributes.canonicalTitle, output.canonicalTitle)
        assertEquals(input.attributes.posterImage, output.posterImage)
    }
}
