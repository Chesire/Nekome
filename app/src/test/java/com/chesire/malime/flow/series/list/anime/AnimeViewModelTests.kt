package com.chesire.malime.flow.series.list.anime

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.repo.SeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AnimeViewModelTests {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val testDispatcher = Dispatchers.Unconfined

    @Test
    fun `updateSeries sends request through the repository`() = runBlocking {
        val mockRepo = mockk<SeriesRepository> {
            coEvery {
                updateSeries(0, 0, UserSeriesStatus.Current)
            } coAnswers {
                mockk()
            }
        }

        val classUnderTest = AnimeViewModel(mockRepo, testDispatcher)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { runBlocking { mockRepo.updateSeries(0, 0, UserSeriesStatus.Current) } }
    }
}
