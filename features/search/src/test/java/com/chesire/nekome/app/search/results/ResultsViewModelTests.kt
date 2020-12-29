package com.chesire.nekome.app.search.results

import com.chesire.nekome.core.AuthCaster
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.seriesflags.SeriesType
import com.chesire.nekome.seriesflags.UserSeriesStatus
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ResultsViewModelTests {
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `trackNewSeries on anime type calls addAnime on the repo`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                addAnime(any(), any())
            } coAnswers {
                Resource.Success(mockk())
            }
            every { getSeries() } returns mockk()
        }
        val mockCaster = mockk<AuthCaster>()
        val mockSettings = mockk<ApplicationSettings> {
            every { defaultSeriesState } returns UserSeriesStatus.Current
        }
        val testObject = ResultsViewModel(mockRepo, mockCaster, mockSettings)

        testObject.trackNewSeries(ResultsData(0, SeriesType.Anime)) {}

        coVerify { mockRepo.addAnime(any(), any()) }
    }

    @Test
    fun `trackNewSeries on manga type calls addManga on the repo`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                addManga(any(), any())
            } coAnswers {
                Resource.Success(mockk())
            }
            every { getSeries() } returns mockk()
        }
        val mockCaster = mockk<AuthCaster>()
        val mockSettings = mockk<ApplicationSettings> {
            every { defaultSeriesState } returns UserSeriesStatus.Current
        }
        val testObject = ResultsViewModel(mockRepo, mockCaster, mockSettings)

        testObject.trackNewSeries(ResultsData(0, SeriesType.Manga)) {}

        coVerify { mockRepo.addManga(any(), any()) }
    }

    @Test
    fun `trackNewSeries on failure to add and could not refresh fires authCaster`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                addManga(any(), any())
            } coAnswers {
                Resource.Error("", Resource.Error.CouldNotRefresh)
            }
            every { getSeries() } returns mockk()
        }
        val mockCaster = mockk<AuthCaster> {
            every { issueRefreshingToken() } just Runs
        }
        val mockSettings = mockk<ApplicationSettings> {
            every { defaultSeriesState } returns UserSeriesStatus.Current
        }
        val testObject = ResultsViewModel(mockRepo, mockCaster, mockSettings)

        testObject.trackNewSeries(ResultsData(0, SeriesType.Manga)) {}

        verify { mockCaster.issueRefreshingToken() }
    }

    @Test
    fun `trackNewSeries on success executes callback`() {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                addAnime(any(), any())
            } coAnswers {
                Resource.Success(mockk())
            }
            every { getSeries() } returns mockk()
        }
        val mockCaster = mockk<AuthCaster>()
        val mockCallback = mockk<(Boolean) -> Unit>(relaxed = true)
        val mockSettings = mockk<ApplicationSettings> {
            every { defaultSeriesState } returns UserSeriesStatus.Current
        }
        val testObject = ResultsViewModel(mockRepo, mockCaster, mockSettings)

        testObject.trackNewSeries(ResultsData(0, SeriesType.Anime), mockCallback)

        verify { mockCallback.invoke(any()) }
    }
}
