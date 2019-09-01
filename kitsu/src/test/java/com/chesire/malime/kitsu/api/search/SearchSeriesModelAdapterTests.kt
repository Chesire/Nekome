package com.chesire.malime.kitsu.api.search

import com.chesire.malime.server.flags.SeriesStatus
import com.chesire.malime.server.flags.SeriesType
import com.chesire.malime.server.flags.Subtype
import com.chesire.malime.server.flags.UserSeriesStatus
import com.chesire.malime.server.models.ImageModel
import com.chesire.malime.kitsu.api.intermediaries.Links
import com.chesire.malime.kitsu.api.intermediaries.SeriesItem
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
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

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

    @Test
    fun `seriesModelsFromSearchResponse null episodeCount falls to chapterCount`() {
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
                    5,
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals(5, actual.totalLength)
    }

    @Test
    fun `seriesModelsFromSearchResponse null episodeCount and chapterCount falls to 0`() {
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
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals(0, actual.totalLength)
    }

    @Test
    fun `seriesModelsFromSearchResponse null posterImage falls to empty ImageModel`() {
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
                    null,
                    ImageModel.empty,
                    null,
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals(ImageModel.empty, actual.posterImage)
    }

    @Test
    fun `seriesModelsFromSearchResponse null coverImage falls to empty ImageModel`() {
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
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals(ImageModel.empty, actual.coverImage)
    }

    @Test
    fun `seriesModelsFromSearchResponse null startDate falls to empty String`() {
        val responseData = listOf(
            SeriesItem(
                0,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "slug",
                    "canonicalTitle",
                    null,
                    "endDate",
                    Subtype.Movie,
                    SeriesStatus.Finished,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals("", actual.startDate)
    }

    @Test
    fun `seriesModelsFromSearchResponse null endDate falls to empty String`() {
        val responseData = listOf(
            SeriesItem(
                0,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "slug",
                    "canonicalTitle",
                    null,
                    null,
                    Subtype.Movie,
                    SeriesStatus.Finished,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val responseLinks = Links("", "", "")
        val response = SearchResponse(responseData, responseLinks)

        val classUnderTest = SearchSeriesModelAdapter()
        val actual = classUnderTest.seriesModelsFromSearchResponse(response).first()

        assertEquals("", actual.endDate)
    }
}
