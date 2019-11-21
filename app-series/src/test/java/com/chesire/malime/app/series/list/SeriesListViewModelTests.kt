package com.chesire.malime.app.series.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.malime.core.AuthCaster
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import com.chesire.malime.testing.CoroutinesMainDispatcherRule
import com.chesire.malime.testing.createSeriesModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SeriesListViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `updateSeries sends request through the repository`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                mockk()
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, { })

        coVerify { mockRepo.updateSeries(0, 0, UserSeriesStatus.Current) }
    }

    @Test
    fun `updateSeries CouldNotRefresh failure notifies through AuthCaster`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("error", Resource.Error.CouldNotRefresh)
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, { })

        verify { mockAuthCaster.issueRefreshingToken() }
    }

    @Test
    fun `updateSeries failure not CouldNotRefresh invokes callback`() {
        var condition = false
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("error", Resource.Error.GenericError)
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current) { condition = true }

        assertTrue(condition)
    }

    @Test
    fun `updateSeries success invokes callback`() {
        var condition = false
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Success(mockk())
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current) { condition = true }

        assertTrue(condition)
    }

    @Test
    fun `deleteSeries CouldNotRefresh failure notifies through AuthCaster`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error("error", Resource.Error.CouldNotRefresh)
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.deleteSeries(createSeriesModel())

        verify { mockAuthCaster.issueRefreshingToken() }
    }

    @Test
    fun `deleteSeries not CouldNotRefresh failure updates live data`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error("error", Resource.Error.GenericError)
            }
            every { series } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<SeriesModel, SeriesListDeleteError>>>() {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteSeries(createSeriesModel())

        verify { mockObserver.onChanged(any()) }
    }
}
