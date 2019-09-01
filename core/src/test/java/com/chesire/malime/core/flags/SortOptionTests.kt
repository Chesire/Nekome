package com.chesire.malime.core.flags

import org.junit.Assert.assertEquals
import org.junit.Test

class SortOptionTests {
    @Test
    fun `forIndex SortOption#Default returns expected value`() {
        assertEquals(com.chesire.malime.core.flags.SortOption.Default, com.chesire.malime.core.flags.SortOption.forIndex(0))
    }

    @Test
    fun `forIndex SortOption#Title returns expected value`() {
        assertEquals(com.chesire.malime.core.flags.SortOption.Title, com.chesire.malime.core.flags.SortOption.forIndex(1))
    }

    @Test
    fun `forIndex SortOption#StartDate returns expected value`() {
        assertEquals(com.chesire.malime.core.flags.SortOption.StartDate, com.chesire.malime.core.flags.SortOption.forIndex(2))
    }

    @Test
    fun `forIndex SortOption#EndDate returns expected value`() {
        assertEquals(com.chesire.malime.core.flags.SortOption.EndDate, com.chesire.malime.core.flags.SortOption.forIndex(3))
    }
}
