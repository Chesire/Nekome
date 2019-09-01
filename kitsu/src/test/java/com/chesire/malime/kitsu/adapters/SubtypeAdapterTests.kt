package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.Subtype
import org.junit.Assert.assertEquals
import org.junit.Test

class SubtypeAdapterTests {
    @Test
    fun `subtypeFromString with 'ONA'`() {
        assertEquals(
            Subtype.ONA,
            SubtypeAdapter().subtypeFromString("ONA")
        )
    }

    @Test
    fun `subtypeFromString with 'OVA'`() {
        assertEquals(
            Subtype.OVA,
            SubtypeAdapter().subtypeFromString("OVA")
        )
    }

    @Test
    fun `subtypeFromString with 'TV'`() {
        assertEquals(
            Subtype.TV,
            SubtypeAdapter().subtypeFromString("TV")
        )
    }

    @Test
    fun `subtypeFromString with 'movie'`() {
        assertEquals(
            Subtype.Movie,
            SubtypeAdapter().subtypeFromString("movie")
        )
    }

    @Test
    fun `subtypeFromString with 'music'`() {
        assertEquals(
            Subtype.Music,
            SubtypeAdapter().subtypeFromString("music")
        )
    }

    @Test
    fun `subtypeFromString with 'special'`() {
        assertEquals(
            Subtype.Special,
            SubtypeAdapter().subtypeFromString("special")
        )
    }

    @Test
    fun `subtypeFromString with 'doujin'`() {
        assertEquals(
            Subtype.Doujin,
            SubtypeAdapter().subtypeFromString("doujin")
        )
    }

    @Test
    fun `subtypeFromString with 'manga'`() {
        assertEquals(
            Subtype.Manga,
            SubtypeAdapter().subtypeFromString("manga")
        )
    }

    @Test
    fun `subtypeFromString with 'manhua'`() {
        assertEquals(
            Subtype.Manhua,
            SubtypeAdapter().subtypeFromString("manhua")
        )
    }

    @Test
    fun `subtypeFromString with 'manhwa'`() {
        assertEquals(
            Subtype.Manhwa,
            SubtypeAdapter().subtypeFromString("manhwa")
        )
    }

    @Test
    fun `subtypeFromString with 'novel'`() {
        assertEquals(
            Subtype.Novel,
            SubtypeAdapter().subtypeFromString("novel")
        )
    }

    @Test
    fun `subtypeFromString with 'oel'`() {
        assertEquals(
            Subtype.OEL,
            SubtypeAdapter().subtypeFromString("oel")
        )
    }

    @Test
    fun `subtypeFromString with 'oneshot'`() {
        assertEquals(
            Subtype.Oneshot,
            SubtypeAdapter().subtypeFromString("oneshot")
        )
    }

    @Test
    fun `subtypeFromString with 'unknown'`() {
        assertEquals(
            Subtype.Unknown,
            SubtypeAdapter().subtypeFromString("unknown")
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#ONA'`() {
        assertEquals(
            "ONA",
            SubtypeAdapter().subtypeToString(Subtype.ONA)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#OVA'`() {
        assertEquals(
            "OVA",
            SubtypeAdapter().subtypeToString(Subtype.OVA)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#TV'`() {
        assertEquals(
            "TV",
            SubtypeAdapter().subtypeToString(Subtype.TV)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Movie'`() {
        assertEquals(
            "movie",
            SubtypeAdapter().subtypeToString(Subtype.Movie)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Music'`() {
        assertEquals(
            "music",
            SubtypeAdapter().subtypeToString(Subtype.Music)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Special'`() {
        assertEquals(
            "special",
            SubtypeAdapter().subtypeToString(Subtype.Special)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Doujin'`() {
        assertEquals(
            "doujin",
            SubtypeAdapter().subtypeToString(Subtype.Doujin)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Manga'`() {
        assertEquals(
            "manga",
            SubtypeAdapter().subtypeToString(Subtype.Manga)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Manhua'`() {
        assertEquals(
            "manhua",
            SubtypeAdapter().subtypeToString(Subtype.Manhua)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Manhwa'`() {
        assertEquals(
            "manhwa",
            SubtypeAdapter().subtypeToString(Subtype.Manhwa)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Novel'`() {
        assertEquals(
            "novel",
            SubtypeAdapter().subtypeToString(Subtype.Novel)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#OEL'`() {
        assertEquals(
            "oel",
            SubtypeAdapter().subtypeToString(Subtype.OEL)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Oneshot'`() {
        assertEquals(
            "oneshot",
            SubtypeAdapter().subtypeToString(Subtype.Oneshot)
        )
    }

    @Test
    fun `subtypeToString with 'Subtype#Unknown'`() {
        assertEquals(
            "unknown",
            SubtypeAdapter().subtypeToString(Subtype.Unknown)
        )
    }
}
