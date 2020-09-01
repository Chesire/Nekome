package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import org.junit.Assert.assertEquals
import org.junit.Test

class LibrarySeriesModelAdapterTests {
    @Test
    fun `seriesModelFromAddResponse amalgamates results into SeriesModel`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    6,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals(0, actual.id)
        assertEquals(1, actual.userId)
        assertEquals(SeriesType.Manga, actual.type)
        assertEquals(Subtype.Oneshot, actual.subtype)
        assertEquals("seriesSlug", actual.slug)
        assertEquals("synopsis", actual.synopsis)
        assertEquals("seriesTitle", actual.title)
        assertEquals(SeriesStatus.TBA, actual.seriesStatus)
        assertEquals(UserSeriesStatus.Planned, actual.userSeriesStatus)
        assertEquals(5, actual.progress)
        assertEquals(6, actual.totalLength)
        assertEquals(ImageModel.empty, actual.posterImage)
        assertEquals(ImageModel.empty, actual.coverImage)
        assertEquals(false, actual.nsfw)
        assertEquals("startDate", actual.startDate)
        assertEquals("endDate", actual.endDate)
    }

    @Test
    fun `seriesModelFromAddResponse null episodeCount falls to chapterCount`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    6,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals(6, actual.totalLength)
    }

    @Test
    fun `seriesModelFromAddResponse null episodeCount and chapterCount falls to 0`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals(0, actual.totalLength)
    }

    @Test
    fun `seriesModelFromAddResponse null posterImage falls to empty ImageModel`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    null,
                    ImageModel.empty,
                    null,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals(ImageModel.empty, actual.posterImage)
    }

    @Test
    fun `seriesModelFromAddResponse null coverImage falls to empty ImageModel`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals(ImageModel.empty, actual.coverImage)
    }

    @Test
    fun `seriesModelFromAddResponse null startDate falls to empty String`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    null,
                    "endDate",
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals("", actual.startDate)
    }

    @Test
    fun `seriesModelFromAddResponse null endDate falls to empty String`() {
        val responseData = LibraryEntry(
            1,
            LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
            LibraryEntry.LibraryRelationships(null, null)
        )
        val seriesData = listOf(
            SeriesItem(
                0,
                SeriesType.Manga,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "synopsis",
                    "seriesTitle",
                    null,
                    null,
                    Subtype.Oneshot,
                    SeriesStatus.TBA,
                    null,
                    null,
                    null,
                    null,
                    false
                )
            )
        )
        val response = AddResponse(responseData, seriesData)

        val classUnderTest = LibrarySeriesModelAdapter()
        val actual = classUnderTest.seriesModelFromAddResponse(response)

        assertEquals("", actual.endDate)
    }
}
