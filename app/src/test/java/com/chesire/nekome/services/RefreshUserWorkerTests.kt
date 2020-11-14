package com.chesire.nekome.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshUserWorkerTests {
    @Test
    fun `doWork null userId returns Result#success early`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { null }
        }
        val testObject = RefreshUserWorker(mockContext, mockParams, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 0) { mockUserRepo.refreshUser() }
    }

    @Test
    fun `doWork refreshUser successful returns Result#success`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
            coEvery { refreshUser() } coAnswers { Resource.Success(mockk()) }
        }
        val testObject = RefreshUserWorker(mockContext, mockParams, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 1) { mockUserRepo.refreshUser() }
    }

    @Test
    fun `doWork refreshUser failure returns Result#retry`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
            coEvery { refreshUser() } coAnswers { Resource.Error("") }
        }
        val testObject = RefreshUserWorker(mockContext, mockParams, mockUserRepo)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
        coVerify(exactly = 1) { mockUserRepo.refreshUser() }
    }
}
