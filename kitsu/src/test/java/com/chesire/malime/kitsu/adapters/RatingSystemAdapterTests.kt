package com.chesire.malime.kitsu.adapters

import com.chesire.malime.core.flags.RatingSystem
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingSystemAdapterTests {
    @Test
    fun `ratingFromString with 'advanced'`() {
        assertEquals(RatingSystem.Advanced, RatingSystemAdapter().ratingFromString("advanced"))
    }

    @Test
    fun `ratingFromString with 'regular'`() {
        assertEquals(RatingSystem.Regular, RatingSystemAdapter().ratingFromString("regular"))
    }

    @Test
    fun `ratingFromString with 'simple'`() {
        assertEquals(RatingSystem.Simple, RatingSystemAdapter().ratingFromString("simple"))
    }

    @Test
    fun `ratingFromString with 'unknown'`() {
        assertEquals(RatingSystem.Unknown, RatingSystemAdapter().ratingFromString("unknown"))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Advanced'`() {
        assertEquals("advanced", RatingSystemAdapter().ratingToString(RatingSystem.Advanced))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Regular'`() {
        assertEquals("regular", RatingSystemAdapter().ratingToString(RatingSystem.Regular))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Simple'`() {
        assertEquals("simple", RatingSystemAdapter().ratingToString(RatingSystem.Simple))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Unknown'`() {
        assertEquals("unknown", RatingSystemAdapter().ratingToString(RatingSystem.Unknown))
    }
}
