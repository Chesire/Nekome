package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.SortOption
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrentSortUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var currentSort: CurrentSortUseCase

    @Before
    fun setup() {
        clearAllMocks()

        currentSort = CurrentSortUseCase(pref)
    }

    @Test
    fun `When invoking, then expected sort option is returned`() = runTest {
        every { pref.sort } returns flowOf(SortOption.Title)

        val result = currentSort()

        assertEquals(SortOption.Title, result)
    }
}
