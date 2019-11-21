package com.chesire.malime.app.series.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.AuthCaster
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import com.chesire.malime.testing.CoroutinesMainDispatcherRule
import com.chesire.malime.testing.createSeriesModel
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
