@file:OptIn(ExperimentalCoroutinesApi::class)

package com.chesire.nekome.binders

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.nekome.datasource.series.UserProvider
import com.chesire.nekome.datasource.user.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserProviderBinderTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `provideUserId returns UserIdResult#Failure if no id available`() = runTest {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns null
        }
        val testObject = UserProviderBinder(mockRepo)

        val actual = testObject.provideUserId()

        assertTrue(actual is UserProvider.UserIdResult.Failure)
    }

    @Test
    fun `provideUserId returns expected id`() = runTest {
        val mockRepo = mockk<UserRepository> {
            coEvery { retrieveUserId() } returns 2
        }
        val testObject = UserProviderBinder(mockRepo)

        val actual = testObject.provideUserId()

        actual as UserProvider.UserIdResult.Success
        assertEquals(2, actual.id)
    }
}
