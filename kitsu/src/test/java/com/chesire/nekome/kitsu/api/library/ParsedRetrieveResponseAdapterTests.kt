package com.chesire.nekome.kitsu.api.library

import com.chesire.nekome.core.flags.SeriesStatus
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.kitsu.api.intermediaries.Links
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ParsedRetrieveResponseAdapterTests {
    @Test
    fun `parseRetrieveResponse amalgamates results into ParsedRetrieveResponse`() {
        val responseData = listOf(
            LibraryEntry(
                1,
                LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
                LibraryEntry.LibraryRelationships(
                    LibraryEntry.LibraryRelationships.RelationshipObject(
                        LibraryEntry.LibraryRelationships.RelationshipObject.RelationshipData(
                            "anime",
                            0
                        )
                    ),
                    null
                )
            )
        )
        val responseIncluded = listOf(
            SeriesItem(
                0,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.OVA,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    6,
                    false
                )
            )
        )
        val responseLinks = Links()
        val response = RetrieveResponse(responseData, responseIncluded, responseLinks)

        val classUnderTest = ParsedRetrieveResponseAdapter()
        val result = classUnderTest.parseRetrieveResponse(response)
        val actual = result.series.first()

        assertEquals(0, actual.id)
        assertEquals(1, actual.userId)
        assertEquals(SeriesType.Anime, actual.type)
        assertEquals(Subtype.OVA, actual.subtype)
        assertEquals("seriesSlug", actual.slug)
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
    fun `parseRetrieveResponse no matching id has empty model`() {
        val responseData = listOf(
            LibraryEntry(
                1,
                LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
                LibraryEntry.LibraryRelationships(
                    LibraryEntry.LibraryRelationships.RelationshipObject(
                        LibraryEntry.LibraryRelationships.RelationshipObject.RelationshipData(
                            "anime",
                            0
                        )
                    ),
                    null
                )
            )
        )
        val responseIncluded = listOf(
            SeriesItem(
                20,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.OVA,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    6,
                    false
                )
            )
        )
        val responseLinks = Links()
        val response = RetrieveResponse(responseData, responseIncluded, responseLinks)

        val classUnderTest = ParsedRetrieveResponseAdapter()
        val actual = classUnderTest.parseRetrieveResponse(response)

        assertTrue(actual.series.isEmpty())
    }

    @Test
    fun `parseRetrieveResponse no relationship has empty model`() {
        val responseData = listOf(
            LibraryEntry(
                1,
                LibraryEntry.LibraryAttributes(UserSeriesStatus.Planned, 5, null, null),
                LibraryEntry.LibraryRelationships(null, null)
            )
        )
        val responseIncluded = listOf(
            SeriesItem(
                20,
                SeriesType.Anime,
                SeriesItem.SeriesAttributes(
                    "seriesSlug",
                    "seriesTitle",
                    "startDate",
                    "endDate",
                    Subtype.OVA,
                    SeriesStatus.TBA,
                    ImageModel.empty,
                    ImageModel.empty,
                    null,
                    6,
                    false
                )
            )
        )
        val responseLinks = Links()
        val response = RetrieveResponse(responseData, responseIncluded, responseLinks)

        val classUnderTest = ParsedRetrieveResponseAdapter()
        val actual = classUnderTest.parseRetrieveResponse(response)

        assertTrue(actual.series.isEmpty())
    }
}
