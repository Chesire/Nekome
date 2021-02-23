package com.chesire.nekome.app.series.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.library.SeriesDomain
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import com.chesire.nekome.testing.createSeriesDomain
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
                updateSeries(0, 0, UserSeriesStatus.Current, 0)
            } coAnswers {
                mockk()
            }
            every { getSeries() } returns mockk()
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, 0, { })

        coVerify { mockRepo.updateSeries(0, 0, UserSeriesStatus.Current, 0) }
    }

    @Test
    fun `updateSeries failure invokes callback`() {
        var condition = false
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current, 0)
            } coAnswers {
                Resource.Error.badRequest("error")
            }
            every { getSeries() } returns mockk()
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, 0) { condition = true }

        assertTrue(condition)
    }

    @Test
    fun `updateSeries success invokes callback`() {
        var condition = false
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current, 0)
            } coAnswers {
                Resource.Success(mockk())
            }
            every { getSeries() } returns mockk()
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, 0) { condition = true }

        assertTrue(condition)
    }

    @Test
    fun `deleteSeries failure updates live data`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                deleteSeries(any())
            } coAnswers {
                Resource.Error.badRequest("error")
            }
            every { getSeries() } returns mockk()
        }
        val mockObserver = mockk<Observer<AsyncState<SeriesDomain, SeriesListDeleteError>>>() {
            every { onChanged(any()) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
        classUnderTest.deletionStatus.observeForever(mockObserver)
        classUnderTest.deleteSeries(createSeriesDomain())

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

        val classUnderTest = SeriesListViewModel(mockRepo)
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

        val classUnderTest = SeriesListViewModel(mockRepo)
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
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
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
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
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
        val mockObserver = mockk<Observer<AsyncState<Any, Any>>>() {
            every { onChanged(capture(slot)) } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo)
        classUnderTest.refreshStatus.observeForever(mockObserver)
        classUnderTest.refreshAllSeries()

        assertTrue(slot.captured is AsyncState.Success)
    }
}
