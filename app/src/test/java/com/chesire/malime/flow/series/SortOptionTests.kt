package com.chesire.malime.flow.series

import org.junit.Assert.assertEquals
import org.junit.Test

class SortOptionTests {
    @Test
    fun `forIndex SortOption#Default returns expected value`() {
        assertEquals(SortOption.Default, SortOption.forIndex(0))
    }

    @Test
    fun `forIndex SortOption#Title returns expected value`() {
        assertEquals(SortOption.Title, SortOption.forIndex(1))
    }

    @Test
    fun `forIndex SortOption#StartDate returns expected value`() {
        assertEquals(SortOption.StartDate, SortOption.forIndex(2))
    }

    @Test
    fun `forIndex SortOption#EndDate returns expected value`() {
        assertEquals(SortOption.EndDate, SortOption.forIndex(3))
    }
}
