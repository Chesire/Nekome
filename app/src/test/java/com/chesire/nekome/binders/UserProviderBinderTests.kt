package com.chesire.nekome.binders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserProviderBinderTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test(expected = IllegalArgumentException::class)
    fun `provideUserId throws exception if no id available`() {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns null
        }
        val testObject = UserProviderBinder(mockRepo)

        runBlocking { testObject.provideUserId() }
    }

    @Test
    fun `provideUserId returns expected id`() {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns 2
        }
        val testObject = UserProviderBinder(mockRepo)

        val result = runBlocking { testObject.provideUserId() }

        assertEquals(2, result)
    }
}
