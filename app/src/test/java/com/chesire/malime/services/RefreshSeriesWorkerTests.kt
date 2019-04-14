package com.chesire.malime.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.malime.core.Resource
import com.chesire.malime.repo.SeriesRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshSeriesWorkerTests {
    @Test
    fun `doWork all success returns Result#success`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            repo = mockRepo
            assertEquals(ListenableWorker.Result.success(), doWork())
        }
    }

    @Test
    fun `doWork failure to refreshAnime returns Result#failure`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Error("test error") }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            repo = mockRepo
            assertEquals(ListenableWorker.Result.retry(), doWork())
        }
    }

    @Test
    fun `doWork failure to refreshManga returns Result#failure`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Error("test error") }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            repo = mockRepo
            assertEquals(ListenableWorker.Result.retry(), doWork())
        }
    }

    @Test
    fun `doWork all failure returns Result#failure`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Error("test error") }
            coEvery { refreshManga() } coAnswers { Resource.Error("test error") }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            repo = mockRepo
            assertEquals(ListenableWorker.Result.retry(), doWork())
        }
    }
}
