package com.chesire.nekome.core.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class SeriesTypeTests {
    @Test
    fun `forId '-1' returns SeriesType#Unknown`() {
        assertEquals(SeriesType.Unknown, SeriesType.forId(-1))
    }

    @Test
    fun `forId '0' returns SeriesType#Anime`() {
        assertEquals(SeriesType.Anime, SeriesType.forId(0))
    }

    @Test
    fun `forId '1' returns SeriesType#Manga`() {
        assertEquals(SeriesType.Manga, SeriesType.forId(1))
    }
}
