package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.ErrorDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesDomain
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UpdateItemUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private lateinit var updateItem: UpdateItemUseCase

    @Before
    fun setup() {
        clearAllMocks()

        updateItem = UpdateItemUseCase(seriesRepo)
    }

    @Test
    fun `When invoking, on success, Ok is returned`() = runTest {
        coEvery {
            seriesRepo.updateSeries(any(), any(), any(), any())
        } returns Ok(createSeriesDomain())

        val result = updateItem(
            UpdateItemModel(
                userSeriesId = 123,
                progress = 456,
                newStatus = UserSeriesStatus.Completed,
                rating = 0
            )
        )

        assertTrue(result is Ok)
    }

    @Test
    fun `When invoking, on failure, Err is returned`() = runTest {
        coEvery {
            seriesRepo.updateSeries(any(), any(), any(), any())
        } returns Err(ErrorDomain.badRequest)

        val result = updateItem(
            UpdateItemModel(
                userSeriesId = 123,
                progress = 456,
                newStatus = UserSeriesStatus.Completed,
                rating = 0
            )
        )

        assertTrue(result is Err)
    }
}
