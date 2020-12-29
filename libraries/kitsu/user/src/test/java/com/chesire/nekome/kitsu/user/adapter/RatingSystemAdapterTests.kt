package com.chesire.nekome.kitsu.user.adapter

import com.chesire.nekome.dataflags.RatingSystem
import org.junit.Assert.assertEquals
import org.junit.Test

class RatingSystemAdapterTests {
    private val adapter = RatingSystemAdapter()

    @Test
    fun `ratingFromString with 'advanced'`() {
        assertEquals(RatingSystem.Advanced, adapter.ratingFromString("advanced"))
    }

    @Test
    fun `ratingFromString with 'regular'`() {
        assertEquals(RatingSystem.Regular, adapter.ratingFromString("regular"))
    }

    @Test
    fun `ratingFromString with 'simple'`() {
        assertEquals(RatingSystem.Simple, adapter.ratingFromString("simple"))
    }

    @Test
    fun `ratingFromString with 'unknown'`() {
        assertEquals(RatingSystem.Unknown, adapter.ratingFromString("unknown"))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Advanced'`() {
        assertEquals("advanced", adapter.ratingToString(RatingSystem.Advanced))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Regular'`() {
        assertEquals("regular", adapter.ratingToString(RatingSystem.Regular))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Simple'`() {
        assertEquals("simple", adapter.ratingToString(RatingSystem.Simple))
    }

    @Test
    fun `ratingToString with 'RatingSystem#Unknown'`() {
        assertEquals("unknown", adapter.ratingToString(RatingSystem.Unknown))
    }
}
