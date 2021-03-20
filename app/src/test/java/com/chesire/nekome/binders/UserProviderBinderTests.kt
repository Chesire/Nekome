package com.chesire.nekome.binders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.nekome.datasource.user.UserRepository
import com.chesire.nekome.library.UserProvider
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserProviderBinderTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `provideUserId returns UserIdResult#Failure if no id available`() = runBlockingTest {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns null
        }
        val testObject = UserProviderBinder(mockRepo)

        val actual = testObject.provideUserId()

        assertTrue(actual is UserProvider.UserIdResult.Failure)
    }

    @Test
    fun `provideUserId returns expected id`() = runBlockingTest {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns 2
        }
        val testObject = UserProviderBinder(mockRepo)

        val actual = testObject.provideUserId()

        actual as UserProvider.UserIdResult.Success
        assertEquals(2, actual.id)
    }
}
