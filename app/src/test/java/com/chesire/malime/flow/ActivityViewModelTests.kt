package com.chesire.malime.flow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chesire.malime.CoroutinesMainDispatcherRule
import com.chesire.malime.LogoutHandler
import com.chesire.malime.R
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.repo.UserRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ActivityViewModelTests {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = CoroutinesMainDispatcherRule()

    @Test
    fun `startingFragment with no accessToken, starts at login`() {
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
            coroutineRule.testDispatcher,
            mockUserRepository
        )

        assertEquals(R.id.detailsFragment, classUnderTest.startingFragment)
    }

    @Test
    fun `startingFragment with accessToken, starts at anime`() {
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
            coroutineRule.testDispatcher,
            mockUserRepository
        )

        assertEquals(R.id.animeFragment, classUnderTest.startingFragment)
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
            coroutineRule.testDispatcher,
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
            coroutineRule.testDispatcher,
            mockUserRepository
        )

        classUnderTest.logout(mockCallback)

        verifyOrder {
            mockLogoutHandler.executeLogout()
            mockCallback.invoke()
        }
    }
}
