package com.chesire.nekome.kitsu.api.search

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchSeriesModelAdapterTests {
    @Test
    fun `seriesModelsFromSearchResponse converts to SeriesModels`() {
        val responseData = listOf(
            SeriesItem(
                0,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "slug",
                    "canonicalTitle",
                    "startDate",
                    "endDate",
                    Subtype.Movie,
                    SeriesStatus.Finished,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    1,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.modelsFromResponse(response).first()

        // Ensure that the expected data exists in the SeriesModel
        assertEquals(0, actual.id)
        assertEquals(0, actual.userId)
        assertEquals(SeriesType.Anime, actual.type)
        assertEquals(Subtype.Movie, actual.subtype)
        assertEquals("slug", actual.slug)
        assertEquals("canonicalTitle", actual.title)
        assertEquals(SeriesStatus.Finished, actual.seriesStatus)
        assertEquals(UserSeriesStatus.Unknown, actual.userSeriesStatus)
        assertEquals(0, actual.progress)
        assertEquals(1, actual.totalLength)
        assertEquals(ImageModel.empty, actual.posterImage)
        assertEquals(ImageModel.empty, actual.coverImage)
        assertEquals(false, actual.nsfw)
        assertEquals("startDate", actual.startDate)
        assertEquals("endDate", actual.endDate)
    }
}
