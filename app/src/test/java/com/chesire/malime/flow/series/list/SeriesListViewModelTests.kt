package com.chesire.malime.flow.series.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.AuthCaster
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.server.Resource
import com.chesire.malime.server.flags.UserSeriesStatus
import com.chesire.malime.repo.SeriesRepository
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
            every { anime } returns mockk()
            every { manga } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, { })

        coVerify { mockRepo.updateSeries(0, 0, UserSeriesStatus.Current) }
    }

    @Test
    fun `updateSeries 401 failure notifies through AuthCaster`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("error", Resource.Error.CouldNotRefresh)
            }
            every { anime } returns mockk()
            every { manga } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current, { })

        verify { mockAuthCaster.issueRefreshingToken() }
    }

    @Test
    fun `updateSeries failure not 401 invokes callback`() {
        var condition = false
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                Resource.Error("error", Resource.Error.GenericError)
            }
            every { anime } returns mockk()
            every { manga } returns mockk()
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
            every { anime } returns mockk()
            every { manga } returns mockk()
        }
        val mockAuthCaster = mockk<AuthCaster>()

        val classUnderTest = SeriesListViewModel(mockRepo, mockAuthCaster)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current) { condition = true }

        assertTrue(condition)
    }
}
