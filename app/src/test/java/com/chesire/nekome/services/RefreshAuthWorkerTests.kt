package com.chesire.nekome.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.server.Resource
import com.chesire.nekome.server.api.AuthApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshAuthWorkerTests {
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

        RefreshAuthWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo

            val result = doWork()

            assertEquals(ListenableWorker.Result.success(), result)
            coVerify(exactly = 0) { userRepo.refreshUser() }
        }
    }

    @Test
    fun `doWork refresh successful returns Result#success`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }
        val mockAuthApi = mockk<AuthApi> {
            coEvery { refresh() } coAnswers { Resource.Success(mockk()) }
        }

        RefreshAuthWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo
            auth = mockAuthApi

            val result = doWork()

            assertEquals(ListenableWorker.Result.success(), result)
            coVerify(exactly = 1) { mockAuthApi.refresh() }
        }
    }

    @Test
    fun `doWork refresh failure returns Result#retry`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { backgroundExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }
        val mockAuthApi = mockk<AuthApi> {
            coEvery { refresh() } coAnswers { Resource.Error("") }
        }

        RefreshAuthWorker(mockContext, mockParams).run {
            userRepo = mockUserRepo
            auth = mockAuthApi

            val result = doWork()

            assertEquals(ListenableWorker.Result.retry(), result)
            coVerify(exactly = 1) { mockAuthApi.refresh() }
        }
    }
}
