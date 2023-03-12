package com.chesire.nekome.core.preferences.flags

import android.content.Context
import com.chesire.nekome.core.R
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeScreenOptionsTests {
    @Test
    fun `getValueMap returns expected map`() {
        val mockContext = mockk<Context> {
            every { getString(R.string.nav_anime) } returns "Anime"
            every { getString(R.string.nav_manga) } returns "Manga"
        }
        val map = HomeScreenOptions.getValueMap(mockContext)

        assertEquals("Anime", map.getValue(0))
        assertEquals("Manga", map.getValue(1))
    }

    @Test
    fun `getFromIndex with non int value returns default option`() {
        assertEquals(
            HomeScreenOptions.Anime,
            HomeScreenOptions.getFromIndex("not an int")
        )
    }

    @Test
    fun `getFromIndex with out of range value returns default option`() {
        assertEquals(
            HomeScreenOptions.Anime,
            HomeScreenOptions.getFromIndex("99")
        )
    }

    @Test
    fun `getFromIndex with '-1' value returns default option`() {
        assertEquals(
            HomeScreenOptions.Anime,
            HomeScreenOptions.getFromIndex("-1")
        )
    }

    @Test
    fun `getFromIndex with '0' value returns Current value`() {
        assertEquals(
            HomeScreenOptions.Anime,
            HomeScreenOptions.getFromIndex("0")
        )
    }

    @Test
    fun `getFromIndex with '1' value returns Completed value`() {
        assertEquals(
            HomeScreenOptions.Manga,
            HomeScreenOptions.getFromIndex("1")
        )
    }
}
