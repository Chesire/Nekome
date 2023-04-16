@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.login.syncing.core

import com.chesire.nekome.datasource.series.SeriesRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SyncSeriesUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private lateinit var syncSeries: SyncSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        syncSeries = SyncSeriesUseCase(seriesRepo)
    }

    @Test
    fun `When invoking, Then anime and manga is refreshed`() = runTest {
        coEvery { seriesRepo.refreshAnime() } returns Resource.Success(listOf())
        coEvery { seriesRepo.refreshManga() } returns Resource.Success(listOf())

        syncSeries()

        coVerify {
            seriesRepo.refreshAnime()
            seriesRepo.refreshManga()
        }
    }
}
