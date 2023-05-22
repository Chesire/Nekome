package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShouldRateSeriesUseCaseTest {

    private val repo = mockk<SeriesRepository>()
    private val pref = mockk<SeriesPreferences>()
    private lateinit var shouldRateSeries: ShouldRateSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        shouldRateSeries = ShouldRateSeriesUseCase(repo, pref)
    }

    @Test
    fun `When invoking, if rateSeriesOfCompletion is disabled, Then false is returned`() = runTest {
        val input = createSeriesDomain(progress = 11, totalLength = 12)
        coEvery { repo.getSeries(123) } returns input
        every { pref.rateSeriesOnCompletion } returns flowOf(false)

        val result = shouldRateSeries(123)

        assertFalse(result)
    }

    @Test
    fun `When invoking, if new progress isn't total length, Then false is returned`() = runTest {
        val input = createSeriesDomain(progress = 10, totalLength = 12)
        coEvery { repo.getSeries(123) } returns input
        every { pref.rateSeriesOnCompletion } returns flowOf(true)

        val result = shouldRateSeries(123)

        assertFalse(result)
    }

    @Test
    fun `When invoking, if new progress is total length, Then true is returned`() = runTest {
        val input = createSeriesDomain(progress = 11, totalLength = 12)
        coEvery { repo.getSeries(123) } returns input
        every { pref.rateSeriesOnCompletion } returns flowOf(true)

        val result = shouldRateSeries(123)

        assertTrue(result)
    }
}
