package com.chesire.nekome.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.core.Resource
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
        val testObject = RefreshSeriesWorker(mockContext, mockParams, mockSeriesRepo, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 0) { mockSeriesRepo.refreshAnime() }
        coVerify(exactly = 0) { mockSeriesRepo.refreshManga() }
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
        val testObject = RefreshSeriesWorker(mockContext, mockParams, mockSeriesRepo, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
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
        val testObject = RefreshSeriesWorker(mockContext, mockParams, mockSeriesRepo, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
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
        val testObject = RefreshSeriesWorker(mockContext, mockParams, mockSeriesRepo, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
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
        val testObject = RefreshSeriesWorker(mockContext, mockParams, mockSeriesRepo, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }
}
