package com.chesire.malime.flow.series.list.anime

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.repo.SeriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AnimeViewModelTests {
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
        }

        val classUnderTest = AnimeViewModel(mockRepo)
        classUnderTest.updateSeries(0, 0, UserSeriesStatus.Current)

        coVerify { mockRepo.updateSeries(0, 0, UserSeriesStatus.Current) }
    }
}
