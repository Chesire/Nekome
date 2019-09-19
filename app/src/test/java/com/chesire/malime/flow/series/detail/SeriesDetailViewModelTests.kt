package com.chesire.malime.flow.series.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.AuthCaster
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.createSeriesModel
import com.chesire.malime.series.SeriesRepository
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SeriesDetailViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `updateModel updates the currently stored model`() {
        val expected = createSeriesModel()
        val mockRepository = mockk<SeriesRepository>()
        val mockAuth = mockk<AuthCaster>()

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.setModel(expected)
        assertEquals(expected.title, classUnderTest.mutableModel.seriesName)
    }
}
