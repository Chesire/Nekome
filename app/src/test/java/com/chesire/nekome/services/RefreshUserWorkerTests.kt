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

        RefreshUserWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo

            assertEquals(ListenableWorker.Result.success(), doWork())
            coVerify(exactly = 0) { userRepo.refreshUser() }
        }
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

        RefreshUserWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo

            assertEquals(ListenableWorker.Result.success(), doWork())
            coVerify(exactly = 1) { userRepo.refreshUser() }
        }
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

        RefreshUserWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo

            assertEquals(ListenableWorker.Result.retry(), doWork())
            coVerify(exactly = 1) { userRepo.refreshUser() }
        }
    }
}
