package com.chesire.nekome.services

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.auth.AccessTokenResult
import com.chesire.nekome.datasource.user.UserRepository
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
                every { serialTaskExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { null }
        }
        val testObject = RefreshAuthWorker(mockContext, mockParams, mockUserRepo, mockk())

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 0) { mockUserRepo.refreshUser() }
    }

    @Test
    fun `doWork refresh successful returns Result#success`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { serialTaskExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }
        val mockRepository = mockk<AccessTokenRepository> {
            coEvery { refresh() } returns AccessTokenResult.Success
        }
        val testObject = RefreshAuthWorker(mockContext, mockParams, mockUserRepo, mockRepository)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
        coVerify(exactly = 1) { mockRepository.refresh() }
    }

    @Test
    fun `doWork refresh failure returns Result#retry`() = runBlocking {
        val mockContext = mockk<Context>()
        val mockParams = mockk<WorkerParameters> {
            every { taskExecutor } returns mockk {
                every { serialTaskExecutor } returns mockk()
            }
        }
        val mockUserRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } coAnswers { 1 }
        }
        val mockRepository = mockk<AccessTokenRepository> {
            coEvery { refresh() } returns AccessTokenResult.CommunicationError
        }
        val testObject = RefreshAuthWorker(mockContext, mockParams, mockUserRepo, mockRepository)

        val result = testObject.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
        coVerify(exactly = 1) { mockRepository.refresh() }
    }
}
