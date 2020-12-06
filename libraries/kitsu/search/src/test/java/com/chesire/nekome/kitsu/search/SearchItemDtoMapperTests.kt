package com.chesire.nekome.kitsu.search

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.search.dto.SearchItemDto
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchItemDtoMapperTests {

    private val map = SearchItemDtoMapper()

    @Test
    fun `toSearchDomain converts SearchItemDto to SearchDomain`() {
        val posterImageInput = createImageModel(
            ImageModel.ImageData("posterTiny", 10, 5),
            ImageModel.ImageData("posterSmall", 20, 10),
            ImageModel.ImageData("posterMedium", 30, 15),
            ImageModel.ImageData("posterLarge", 40, 20),
        )
        val input = SearchItemDto(
            10,
            SeriesType.Anime,
            SearchItemDto.Attributes(
                "synopsis",
                "canonicalTitle",
                Subtype.Movie,
                posterImageInput
            )
        )

        val output = map.toSearchDomain(input)

        assertEquals(input.id, output.id)
        assertEquals(input.type, output.type)
        assertEquals(input.attributes.synopsis, output.synopsis)
        assertEquals(input.attributes.canonicalTitle, output.canonicalTitle)
        assertEquals(input.attributes.subtype, output.subtype)
        assertEquals(input.attributes.posterImage, output.posterImage)
    }
}
