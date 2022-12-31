package com.chesire.nekome.app.search

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.datasource.search.SearchDomain
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchDomainMapperTests {

    private lateinit var map: SearchDomainMapper

    @Before
    fun setup() {
        map = SearchDomainMapper()
    }

    @Test
    fun `toSeriesModel converts SearchDomain to SeriesModel`() {
        val posterImageInput = createImageModel(
            ImageModel.ImageData("posterTiny", 10, 5),
            ImageModel.ImageData("posterSmall", 20, 10),
            ImageModel.ImageData("posterMedium", 30, 15),
            ImageModel.ImageData("posterLarge", 40, 20),
        )
        val input = SearchDomain(
            25,
            SeriesType.Manga,
            "synopsis",
            "canonicalTitle",
            Subtype.Manhua,
            posterImageInput
        )

        val output = map.toSearchModel(input)

        assertEquals(input.id, output.id)
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.synopsis, output.synopsis)
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.canonicalTitle, output.canonicalTitle)
    }
}
