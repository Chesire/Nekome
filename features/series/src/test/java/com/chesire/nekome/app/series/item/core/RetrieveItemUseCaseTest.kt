package com.chesire.nekome.app.series.item.core

import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RetrieveItemUseCaseTest {

    private val seriesRepo = mockk<SeriesRepository>()
    private lateinit var retrieveItem: RetrieveItemUseCase

    @Before
    fun setup() {
        clearAllMocks()

        retrieveItem = RetrieveItemUseCase(seriesRepo)
    }

    @Test
    fun `When invoking, series for the passed in id is returned`() = runTest {
        val expected = createSeriesDomain()
        coEvery { seriesRepo.getSeries(123) } returns expected

        val result = retrieveItem(123)

        assertEquals(expected, result)
    }
}
