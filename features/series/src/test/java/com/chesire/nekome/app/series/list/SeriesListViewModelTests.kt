package com.chesire.nekome.app.series.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.core.models.SeriesModel
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import com.chesire.nekome.testing.createSeriesModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
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
            every { getSeries() } returns mockk()
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
            every { getSeries() } returns mockk()
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
            every { getSeries() } returns mockk()
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
            every { getSeries() } returns mockk()
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
            every { getSeries() } returns mockk()
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
            every { getSeries() } returns mockk()
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

    @Test
    fun `refreshAllSeries calls refreshAnime in repo`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                refreshAnime()
            } coAnswers {
                mockk()
            }
            coEvery {
                refreshManga()
            } coAnswers {
                mockk()
            }
            every { getSeries() } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.refreshAllSeries()

        coVerify { mockRepo.refreshAnime() }
    }

    @Test
    fun `refreshAllSeries calls refreshManga in repo`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                refreshAnime()
            } coAnswers {
                mockk()
            }
            coEvery {
                refreshManga()
            } coAnswers {
                mockk()
            }
            every { getSeries() } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.refreshAllSeries()

        coVerify { mockRepo.refreshManga() }
    }

    @Test
    fun `refreshAllSeries posts error if anime call fails`() {
        val slot = slot<AsyncState<Any, Any>>()
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                refreshAnime()
            } coAnswers {
                Resource.Error("")
            }
            coEvery {
                refreshManga()
            } coAnswers {
                Resource.Success(emptyList())
            }
            every { getSeries() } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.refreshStatus.observeForever(mockObserver)
        classUnderTest.refreshAllSeries()

        assertTrue(slot.captured is AsyncState.Error)
    }

    @Test
    fun `refreshAllSeries posts error if manga call fails`() {
        val slot = slot<AsyncState<Any, Any>>()
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                refreshAnime()
            } coAnswers {
                Resource.Success(emptyList())
            }
            coEvery {
                refreshManga()
            } coAnswers {
                Resource.Error("")
            }
            every { getSeries() } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.refreshStatus.observeForever(mockObserver)
        classUnderTest.refreshAllSeries()

        assertTrue(slot.captured is AsyncState.Error)
    }

    @Test
    fun `refreshAllSeries posts success if both calls are successful`() {
        val slot = slot<AsyncState<Any, Any>>()
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                refreshAnime()
            } coAnswers {
                Resource.Success(emptyList())
            }
            coEvery {
                refreshManga()
            } coAnswers {
                Resource.Success(emptyList())
            }
            every { getSeries() } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.refreshStatus.observeForever(mockObserver)
        classUnderTest.refreshAllSeries()

        assertTrue(slot.captured is AsyncState.Success)
    }
}
