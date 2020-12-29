package com.chesire.nekome.kitsu.adapters

import com.chesire.nekome.dataflags.SeriesType
import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesTypeAdapterTests {
    @Test
    fun `seriesTypeFromString with 'anime'`() {
        assertEquals(
            SeriesType.Anime,
            SeriesTypeAdapter().seriesTypeFromString("anime")
        )
    }

    @Test
    fun `seriesTypeFromString with 'manga'`() {
        assertEquals(
            SeriesType.Manga,
            SeriesTypeAdapter().seriesTypeFromString("manga")
        )
    }

    @Test
    fun `seriesTypeFromString with 'unknown'`() {
        assertEquals(
            SeriesType.Unknown,
            SeriesTypeAdapter().seriesTypeFromString("unknown")
        )
    }

    @Test
    fun `seriesTypeToString with 'SeriesType#Anime'`() {
        assertEquals(
            "anime",
            SeriesTypeAdapter().seriesTypeToString(SeriesType.Anime)
        )
    }

    @Test
    fun `seriesTypeToString with 'SeriesType#Manga'`() {
        assertEquals(
            "manga",
            SeriesTypeAdapter().seriesTypeToString(SeriesType.Manga)
        )
    }

    @Test
    fun `seriesTypeToString with 'SeriesType#Unknown'`() {
        assertEquals(
            "unknown",
            SeriesTypeAdapter().seriesTypeToString(SeriesType.Unknown)
        )
    }
}
