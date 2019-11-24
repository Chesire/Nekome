package com.chesire.nekome.kitsu.extensions

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ListExtensionsTests {
    @Test
    fun `convertToSeriesModels converts list to SeriesModels`() {
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

        val actual = responseData.convertToSeriesModels().first()

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
    fun `convertToSeriesModels null episodeCount falls to chapterCount`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals(5, actual.totalLength)
    }

    @Test
    fun `convertToSeriesModels null episodeCount and chapterCount falls to 0`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals(0, actual.totalLength)
    }

    @Test
    fun `convertToSeriesModels null posterImage falls to empty ImageModel`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals(ImageModel.empty, actual.posterImage)
    }

    @Test
    fun `convertToSeriesModels null coverImage falls to empty ImageModel`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals(ImageModel.empty, actual.coverImage)
    }

    @Test
    fun `convertToSeriesModels null startDate falls to empty String`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals("", actual.startDate)
    }

    @Test
    fun `convertToSeriesModels null endDate falls to empty String`() {
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

        val actual = responseData.convertToSeriesModels().first()

        assertEquals("", actual.endDate)
    }
}
