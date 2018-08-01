package com.chesire.malime.core.flags

import junit.framework.Assert
import org.junit.Test

// The tests for the Mal id are not currently in, as i'm unsure what the values actually are
class SubtypeTests {
    /*
    Kitsu tests
     */
    @Test
    fun `get subtype for unexpected Kitsu string of "unexpected", returns Unknown subtype`() {
        Assert.assertEquals(Subtype.Unknown, Subtype.getSubtypeForKitsuString("unexpected"))
    }

    @Test
    fun `get subtype for Kitsu string of "unknown", returns Unknown subtype`() {
        Assert.assertEquals(Subtype.Unknown, Subtype.getSubtypeForKitsuString("unknown"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "ONA", returns ONA subtype`() {
        Assert.assertEquals(Subtype.ONA, Subtype.getSubtypeForKitsuString("ONA"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "OVA", returns OVA subtype`() {
        Assert.assertEquals(Subtype.OVA, Subtype.getSubtypeForKitsuString("OVA"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "TV", returns TV subtype`() {
        Assert.assertEquals(Subtype.TV, Subtype.getSubtypeForKitsuString("TV"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "movie", returns Movie subtype`() {
        Assert.assertEquals(Subtype.Movie, Subtype.getSubtypeForKitsuString("movie"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "music", returns Music subtype`() {
        Assert.assertEquals(Subtype.Music, Subtype.getSubtypeForKitsuString("music"))
    }

    @Test
    fun `get anime subtype for Kitsu string of "special", returns Special subtype`() {
        Assert.assertEquals(Subtype.Special, Subtype.getSubtypeForKitsuString("special"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "doujin", returns Doujin subtype`() {
        Assert.assertEquals(Subtype.Doujin, Subtype.getSubtypeForKitsuString("doujin"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "manga", returns Manga subtype`() {
        Assert.assertEquals(Subtype.Manga, Subtype.getSubtypeForKitsuString("manga"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "manhua", returns Manhua subtype`() {
        Assert.assertEquals(Subtype.Manhua, Subtype.getSubtypeForKitsuString("manhua"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "manhwa", returns Manhwa subtype`() {
        Assert.assertEquals(Subtype.Manhwa, Subtype.getSubtypeForKitsuString("manhwa"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "novel", returns Novel subtype`() {
        Assert.assertEquals(Subtype.Novel, Subtype.getSubtypeForKitsuString("novel"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "oel", returns OEL subtype`() {
        Assert.assertEquals(Subtype.OEL, Subtype.getSubtypeForKitsuString("oel"))
    }

    @Test
    fun `get manga subtype for Kitsu string of "oneshot", returns Oneshot subtype`() {
        Assert.assertEquals(Subtype.Oneshot, Subtype.getSubtypeForKitsuString("oneshot"))
    }

    /*
    Internal ID tests
     */

    @Test
    fun `get subtype for unexpected internal id of 999, returns Unknown subtype`() {
        Assert.assertEquals(Subtype.Unknown, Subtype.getSubtypeForInternalId(999))
    }

    @Test
    fun `get subtype for internal id of 0, returns Unknown subtype`() {
        Assert.assertEquals(Subtype.Unknown, Subtype.getSubtypeForInternalId(0))
    }

    @Test
    fun `get anime subtype for internal id of 1, returns ONA subtype`() {
        Assert.assertEquals(Subtype.ONA, Subtype.getSubtypeForInternalId(1))
    }

    @Test
    fun `get anime subtype for internal id of 2, returns OVA subtype`() {
        Assert.assertEquals(Subtype.OVA, Subtype.getSubtypeForInternalId(2))
    }

    @Test
    fun `get anime subtype for internal id of 3, returns TV subtype`() {
        Assert.assertEquals(Subtype.TV, Subtype.getSubtypeForInternalId(3))
    }

    @Test
    fun `get anime subtype for internal id of 4, returns Movie subtype`() {
        Assert.assertEquals(Subtype.Movie, Subtype.getSubtypeForInternalId(4))
    }

    @Test
    fun `get anime subtype for internal id of 5, returns Music subtype`() {
        Assert.assertEquals(Subtype.Music, Subtype.getSubtypeForInternalId(5))
    }

    @Test
    fun `get anime subtype for internal id of 6, returns Special subtype`() {
        Assert.assertEquals(Subtype.Special, Subtype.getSubtypeForInternalId(6))
    }

    @Test
    fun `get manga subtype for internal id of 7, returns Doujin subtype`() {
        Assert.assertEquals(Subtype.Doujin, Subtype.getSubtypeForInternalId(7))
    }

    @Test
    fun `get manga subtype for internal id of 8, returns Manga subtype`() {
        Assert.assertEquals(Subtype.Manga, Subtype.getSubtypeForInternalId(8))
    }

    @Test
    fun `get manga subtype for internal id of 9, returns Manhua subtype`() {
        Assert.assertEquals(Subtype.Manhua, Subtype.getSubtypeForInternalId(9))
    }

    @Test
    fun `get manga subtype for internal id of 10, returns Manhwa subtype`() {
        Assert.assertEquals(Subtype.Manhwa, Subtype.getSubtypeForInternalId(10))
    }

    @Test
    fun `get manga subtype for internal id of 11, returns Novel subtype`() {
        Assert.assertEquals(Subtype.Novel, Subtype.getSubtypeForInternalId(11))
    }

    @Test
    fun `get manga subtype for internal id of 12, returns OEL subtype`() {
        Assert.assertEquals(Subtype.OEL, Subtype.getSubtypeForInternalId(12))
    }

    @Test
    fun `get manga subtype for internal id of 13, returns Oneshot subtype`() {
        Assert.assertEquals(Subtype.Oneshot, Subtype.getSubtypeForInternalId(13))
    }
}