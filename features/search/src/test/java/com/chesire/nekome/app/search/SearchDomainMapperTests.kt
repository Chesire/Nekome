package com.chesire.nekome.app.search

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.search.api.SearchDomain
import com.chesire.nekome.testing.createImageModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchDomainMapperTests {

    private val map = SearchDomainMapper()

    @Test
    fun `toSeriesModel converts SearchDomain to SeriesModel`() {
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
        val input = SearchDomain(
            25,
            SeriesType.Manga,
            "slug",
            "synopsis",
            "canonicalTitle",
            "averageRating",
            "startDate",
            "endDate",
            Subtype.Manhua,
            SeriesStatus.Finished,
            posterImageInput,
            coverImageInput,
            10,
            0,
            true
        )

        val output = map.toSeriesModel(input)

        assertEquals(input.id, output.id)
        assertEquals(0, output.userId) // Remove this later on
        assertEquals(input.type, output.type)
        assertEquals(input.subtype, output.subtype)
        assertEquals(input.slug, output.slug)
        assertEquals(input.synopsis, output.synopsis)
        assertEquals(input.canonicalTitle, output.title)
        assertEquals(input.status, output.seriesStatus)
        assertEquals(UserSeriesStatus.Unknown, output.userSeriesStatus) // Remove this later on
        assertEquals(0, output.progress) // Remove this later on
        assertEquals(0, output.totalLength) // Remove this later on
        assertEquals(input.posterImage, output.posterImage)
        assertEquals(input.coverImage, output.coverImage)
        assertEquals(input.nsfw, output.nsfw)
        assertEquals(input.startDate, output.startDate)
        assertEquals(input.endDate, output.endDate)
    }
}
