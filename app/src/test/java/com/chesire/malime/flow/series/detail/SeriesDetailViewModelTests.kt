package com.chesire.malime.flow.series.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.AuthCaster
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.createSeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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

    @Test
    fun `deleteModel posts loading to the status`() {
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockAuth = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<SeriesModel, SeriesDetailError>>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteModel(mockk())

        verify { mockObserver.onChanged(any<AsyncState.Loading<SeriesModel, SeriesDetailError>>()) }
    }

    @Test
    fun `deleteModel notifies authCaster on token failure`() {
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error("", Resource.Error.CouldNotRefresh)
            }
        }
        val mockAuth = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }
        val mockObserver = mockk<Observer<AsyncState<SeriesModel, SeriesDetailError>>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteModel(mockk())

        verify { mockAuth.issueRefreshingToken() }
    }

    @Test
    fun `deleteModel posts error to the status on failure`() {
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error("")
            }
        }
        val mockAuth = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<SeriesModel, SeriesDetailError>>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteModel(mockk())

        verify { mockObserver.onChanged(any<AsyncState.Error<SeriesModel, SeriesDetailError>>()) }
    }

    @Test
    fun `deleteModel posts success to the status on success`() {
        val mockRepository = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Success(Any())
            }
        }
        val mockAuth = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<SeriesModel, SeriesDetailError>>> {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesDetailViewModel(
            mockRepository,
            mockAuth,
            coroutineRule.testDispatcher
        )
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteModel(mockk())

        verify { mockObserver.onChanged(any<AsyncState.Success<SeriesModel, SeriesDetailError>>()) }
    }
}
