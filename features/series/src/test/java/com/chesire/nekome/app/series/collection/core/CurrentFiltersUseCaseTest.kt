package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.preferences.SeriesPreferences
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrentFiltersUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var currentFilters: CurrentFiltersUseCase

    @Before
    fun setup() {
        clearAllMocks()

        currentFilters = CurrentFiltersUseCase(pref)
    }

    @Test
    fun `When invoking, then expected map of filters is returned`() = runTest {
        every { pref.filter } returns flowOf(
            mapOf(
                -1 to false,
                0 to true,
                1 to true,
                2 to true,
                3 to false,
                4 to false
            )
        )
        val expected = mapOf(
            UserSeriesStatus.Current to true,
            UserSeriesStatus.Completed to true,
            UserSeriesStatus.OnHold to true,
            UserSeriesStatus.Dropped to false,
            UserSeriesStatus.Planned to false
        )

        val result = currentFilters()

        assertEquals(expected, result)
    }
}
