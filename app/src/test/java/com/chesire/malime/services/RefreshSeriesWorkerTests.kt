package com.chesire.malime.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.malime.account.UserRepository
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshSeriesWorkerTests {
    @Test
    fun `doWork null userId returns Result#success early`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockSeriesRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { null }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            seriesRepo = mockSeriesRepo
            userRepo = mockUserRepo

            assertEquals(ListenableWorker.Result.success(), doWork())
            coVerify(exactly = 0) { mockSeriesRepo.refreshAnime() }
            coVerify(exactly = 0) { mockSeriesRepo.refreshManga() }
        }
    }

    @Test
    fun `doWork all success returns Result#success`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockSeriesRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            seriesRepo = mockSeriesRepo
            userRepo = mockUserRepo

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
        val mockSeriesRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Error("test error") }
            coEvery { refreshManga() } coAnswers { Resource.Success(mockk()) }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            seriesRepo = mockSeriesRepo
            userRepo = mockUserRepo

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
        val mockSeriesRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Success(mockk()) }
            coEvery { refreshManga() } coAnswers { Resource.Error("test error") }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            seriesRepo = mockSeriesRepo
            userRepo = mockUserRepo

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
        val mockSeriesRepo = mockk<SeriesRepository> {
            coEvery { refreshAnime() } coAnswers { Resource.Error("test error") }
            coEvery { refreshManga() } coAnswers { Resource.Error("test error") }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }

        RefreshSeriesWorker(mockContext, mockParams).run {
            seriesRepo = mockSeriesRepo
            userRepo = mockUserRepo

            assertEquals(ListenableWorker.Result.retry(), doWork())
        }
    }
}
