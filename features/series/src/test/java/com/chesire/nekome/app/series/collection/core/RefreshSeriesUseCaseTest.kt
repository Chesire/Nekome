@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RefreshSeriesUseCaseTest {

    private val repo = mockk<SeriesRepository>()
    private lateinit var refreshSeries: RefreshSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        refreshSeries = RefreshSeriesUseCase(repo)
    }

    @Test
    fun `When invoking, on all success calls, then Ok is returned`() = runTest {
        coEvery { repo.refreshAnime() } returns Resource.Success(emptyList())
        coEvery { repo.refreshManga() } returns Resource.Success(emptyList())

        val result = refreshSeries()

        assertTrue(result is Ok)
    }

    @Test
    fun `When invoking, on just anime success call, then Err is returned`() = runTest {
        coEvery { repo.refreshAnime() } returns Resource.Success(emptyList())
        coEvery { repo.refreshManga() } returns Resource.Error("")

        val result = refreshSeries()

        assertTrue(result is Err)
    }

    @Test
    fun `When invoking, on just manga success call, then Err is returned`() = runTest {
        coEvery { repo.refreshAnime() } returns Resource.Error("")
        coEvery { repo.refreshManga() } returns Resource.Success(emptyList())

        val result = refreshSeries()

        assertTrue(result is Err)
    }

    @Test
    fun `When invoking, on all failure call, then Err is returned`() = runTest {
        coEvery { repo.refreshAnime() } returns Resource.Error("")
        coEvery { repo.refreshManga() } returns Resource.Error("")

        val result = refreshSeries()

        assertTrue(result is Err)
    }
}
