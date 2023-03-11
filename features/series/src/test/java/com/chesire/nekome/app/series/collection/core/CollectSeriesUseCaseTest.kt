@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CollectSeriesUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private lateinit var collectSeries: CollectSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        collectSeries = CollectSeriesUseCase(seriesRepo)
    }

    @Test
    fun `When invoking, then flow of series is returned`() = runTest {
        val expected = flowOf(listOf(createSeriesDomain()))
        coEvery { seriesRepo.getSeries() } returns expected

        val result = collectSeries()

        assertEquals(expected, result)
    }
}
