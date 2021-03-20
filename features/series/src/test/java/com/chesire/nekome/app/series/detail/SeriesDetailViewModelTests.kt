package com.chesire.nekome.app.series.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import com.chesire.nekome.testing.createSeriesDomain
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SeriesDetailViewModelTests {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `updateModel updates the currently stored model`() {
        val expected = createSeriesDomain()
        val mockRepository = mockk<SeriesRepository>()

        val classUnderTest = SeriesDetailViewModel(
            SavedStateHandle(mapOf(MODEL_ID to expected)),
            mockRepository,
            coroutineRule.testDispatcher
        )

        assertEquals(expected.canonicalTitle, classUnderTest.mutableModel.seriesName)
    }

    @Test
    fun `sendUpdate on error, posts Error`() {
        val slot = slot<AsyncState<MutableSeriesModel, SeriesDetailError>>()
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                updateSeries(any(), any(), any(), any())
            } coAnswers {
                Resource.Error.badRequest("")
            }
        }
        val mockObserver = mockk<Observer<AsyncState<MutableSeriesModel, SeriesDetailError>>> {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            SavedStateHandle(mapOf(MODEL_ID to createSeriesDomain())),
            mockRepository,
            coroutineRule.testDispatcher
        )
        classUnderTest.updatingStatus.observeForever(mockObserver)
        classUnderTest.sendUpdate(classUnderTest.mutableModel)

        assertTrue(slot.captured is AsyncState.Error)
    }

    @Test
    fun `sendUpdate on success, posts Success`() {
        val slot = slot<AsyncState<MutableSeriesModel, SeriesDetailError>>()
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                updateSeries(any(), any(), any(), any())
            } coAnswers {
                Resource.Success(createSeriesDomain())
            }
        }
        val mockObserver = mockk<Observer<AsyncState<MutableSeriesModel, SeriesDetailError>>> {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            SavedStateHandle(mapOf(MODEL_ID to createSeriesDomain())),
            mockRepository,
            coroutineRule.testDispatcher
        )
        classUnderTest.updatingStatus.observeForever(mockObserver)
        classUnderTest.sendUpdate(classUnderTest.mutableModel)

        assertTrue(slot.captured is AsyncState.Success)
    }
}
