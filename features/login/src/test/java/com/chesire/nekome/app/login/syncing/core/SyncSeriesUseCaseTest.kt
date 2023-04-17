@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.login.syncing.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Ok
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
        coEvery { seriesRepo.refreshAnime() } returns Ok(listOf())
        coEvery { seriesRepo.refreshManga() } returns Ok(listOf())

        syncSeries()

        coVerify {
            seriesRepo.refreshAnime()
            seriesRepo.refreshManga()
        }
    }
}
