package com.chesire.nekome.app.series.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.series.SeriesRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import com.chesire.nekome.testing.createSeriesModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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

    @Test
    fun `sendUpdate on CouldNotRefresh error, notifies AuthCaster`() {
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                updateSeries(any(), any(), any())
            } coAnswers {
                Resource.Error("", Resource.Error.CouldNotRefresh)
            }
        }
        val mockAuth = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.setModel(createSeriesModel())
        classUnderTest.sendUpdate(classUnderTest.mutableModel)

        verify { mockAuth.issueRefreshingToken() }
    }

    @Test
    fun `sendUpdate on error, posts Error`() {
        val slot = slot<AsyncState<MutableSeriesModel, SeriesDetailError>>()
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                updateSeries(any(), any(), any())
            } coAnswers {
                Resource.Error("", Resource.Error.GenericError)
            }
        }
        val mockAuth = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }
        val mockObserver = mockk<Observer<AsyncState<MutableSeriesModel, SeriesDetailError>>> {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.updatingStatus.observeForever(mockObserver)
        classUnderTest.setModel(createSeriesModel())
        classUnderTest.sendUpdate(classUnderTest.mutableModel)

        assertTrue(slot.captured is AsyncState.Error)
    }

    @Test
    fun `sendUpdate on success, posts Success`() {
        val slot = slot<AsyncState<MutableSeriesModel, SeriesDetailError>>()
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                updateSeries(any(), any(), any())
            } coAnswers {
                Resource.Success(createSeriesModel())
            }
        }
        val mockAuth = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }
        val mockObserver = mockk<Observer<AsyncState<MutableSeriesModel, SeriesDetailError>>> {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.updatingStatus.observeForever(mockObserver)
        classUnderTest.setModel(createSeriesModel())
        classUnderTest.sendUpdate(classUnderTest.mutableModel)

        assertTrue(slot.captured is AsyncState.Success)
    }
}
