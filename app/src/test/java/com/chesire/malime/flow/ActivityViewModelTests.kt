package com.chesire.malime.flow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.LogoutHandler
import com.chesire.malime.account.UserRepository
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ActivityViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()
    private val testDispatcher = coroutineRule.testDispatcher

    @Test
    fun `userLoggedIn failure returns false`() {
        val mockAuthProvider = mockk<AuthProvider> {
            every { accessToken } returns ""
        }
        val mockLogoutHandler = mockk<LogoutHandler>()
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAuthProvider,
            mockLogoutHandler,
            testDispatcher,
            mockUserRepository
        )

        assertFalse(classUnderTest.userLoggedIn)
    }

    @Test
    fun `userLoggedIn success returns true`() {
        val mockAuthProvider = mockk<AuthProvider> {
            every { accessToken } returns "access token"
        }
        val mockLogoutHandler = mockk<LogoutHandler>()
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAuthProvider,
            mockLogoutHandler,
            testDispatcher,
            mockUserRepository
        )

        assertTrue(classUnderTest.userLoggedIn)
    }

    @Test
    fun `logout tells the logoutHandler to execute`() {
        val mockAuthProvider = mockk<AuthProvider>()
        val mockLogoutHandler = mockk<LogoutHandler> {
            every { executeLogout() } just Runs
        }
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAuthProvider,
            mockLogoutHandler,
            testDispatcher,
            mockUserRepository
        )

        classUnderTest.logout { }

        verify { mockLogoutHandler.executeLogout() }
    }

    @Test
    fun `logout executes callback after logoutHandler`() {
        val mockAuthProvider = mockk<AuthProvider>()
        val mockLogoutHandler = mockk<LogoutHandler> {
            every { executeLogout() } just Runs
        }
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }
        val mockCallback = mockk<() -> Unit>(relaxed = true)

        val classUnderTest = ActivityViewModel(
            mockAuthProvider,
            mockLogoutHandler,
            testDispatcher,
            mockUserRepository
        )

        classUnderTest.logout(mockCallback)

        verifyOrder {
            mockLogoutHandler.executeLogout()
            mockCallback.invoke()
        }
    }
}
