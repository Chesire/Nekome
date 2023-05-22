package com.chesire.nekome.app.series.collection.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesDomain
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IncrementSeriesUseCaseTest {

    private val repo = mockk<SeriesRepository>()
    private lateinit var incrementSeries: IncrementSeriesUseCase

    @Before
    fun setup() {
        clearAllMocks()

        incrementSeries = IncrementSeriesUseCase(repo)
    }

    @Test
    fun `When invoking, on success update series call, then Ok is returned`() = runTest {
        val domain = createSeriesDomain()
        coEvery { repo.getSeries(any()) } returns domain
        coEvery { repo.updateSeries(any(), any(), any(), any()) } returns Ok(domain)

        val result = incrementSeries(123, null)

        assertTrue(result is Ok)
    }

    @Test
    fun `When invoking, on failure update series call, then Ok is returned`() = runTest {
        val domain = createSeriesDomain()
        coEvery { repo.getSeries(any()) } returns domain
        coEvery {
            repo.updateSeries(
                any(),
                any(),
                any(),
                any()
            )
        } returns Err(ErrorDomain.badRequest)

        val result = incrementSeries(123, null)

        assertTrue(result is Err)
    }

    @Test
    fun `When invoking, if rating is provided, then new rating is applied`() = runTest {
        val domain = createSeriesDomain()
        coEvery { repo.getSeries(any()) } returns domain
        coEvery { repo.updateSeries(any(), any(), any(), any()) } returns Ok(domain)

        val result = incrementSeries(123, 5)

        coVerify { repo.updateSeries(any(), domain.progress + 1, UserSeriesStatus.Current, 5) }
        assertTrue(result is Ok)
    }
}
