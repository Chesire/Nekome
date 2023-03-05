@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.app.series.SeriesPreferences
import com.chesire.nekome.core.flags.UserSeriesStatus
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateFiltersUseCaseTest {

    private val pref = mockk<SeriesPreferences>()
    private lateinit var updateFilters: UpdateFiltersUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateFilters = UpdateFiltersUseCase(pref)
    }

    @Test
    fun `When invoking, update filters with new map`() = runTest {
        coEvery { pref.updateFilter(any()) } just runs
        val input = mapOf(
            UserSeriesStatus.Current to false,
            UserSeriesStatus.Completed to true,
            UserSeriesStatus.OnHold to false,
            UserSeriesStatus.Dropped to false,
            UserSeriesStatus.Planned to false
        )
        val expected = mapOf(
            0 to false,
            1 to true,
            2 to false,
            3 to false,
            4 to false
        )

        updateFilters(input)

        coVerify { pref.updateFilter(expected) }
    }
}
