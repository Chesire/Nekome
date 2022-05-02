package com.chesire.nekome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import com.chesire.nekome.core.settings.ApplicationSettings
import com.chesire.nekome.datasource.auth.AccessTokenRepository
import com.chesire.nekome.datasource.user.UserRepository
import com.chesire.nekome.testing.CoroutinesMainDispatcherRule
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
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

    private val settings = mockk<ApplicationSettings>(relaxed = true)

    @Test
    fun `userLoggedIn failure returns false`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository> {
            every { accessToken } returns ""
        }
        val mockLogoutHandler = mockk<LogoutHandler>()
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAccessTokenRepository,
            mockLogoutHandler,
            settings,
            testDispatcher,
            mockUserRepository
        )

        assertFalse(classUnderTest.userLoggedIn)
    }

    @Test
    fun `userLoggedIn success returns true`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository> {
            every { accessToken } returns "access token"
        }
        val mockLogoutHandler = mockk<LogoutHandler>()
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAccessTokenRepository,
            mockLogoutHandler,
            settings,
            testDispatcher,
            mockUserRepository
        )

        assertTrue(classUnderTest.userLoggedIn)
    }

    @Test
    fun `logout tells the logoutHandler to execute`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository>() {
            every { accessToken } returns "access token"
        }
        val mockLogoutHandler = mockk<LogoutHandler> {
            every { executeLogout() } just Runs
        }
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAccessTokenRepository,
            mockLogoutHandler,
            settings,
            testDispatcher,
            mockUserRepository
        )

        classUnderTest.logout()

        verify { mockLogoutHandler.executeLogout() }
    }


    @Test
    fun `if user is not logged it then details screen navigation event is emited`() {
        val mockAccessTokenRepository = mockk<AccessTokenRepository>() {
            every { accessToken } returns ""
        }
        val mockLogoutHandler = mockk<LogoutHandler> {
            every { executeLogout() } just Runs
        }
        val mockUserRepository = mockk<UserRepository> {
            every { user } returns mockk()
        }

        val classUnderTest = ActivityViewModel(
            mockAccessTokenRepository,
            mockLogoutHandler,
            settings,
            testDispatcher,
            mockUserRepository
        )

        val mockObserver = mockk<Observer<NavDirections>> {
            every { onChanged(any()) } just Runs
        }

        classUnderTest.navigation.observeForever(mockObserver)

        verify {
            mockObserver.onChanged(OverviewNavGraphDirections.globalToDetailsFragment())
        }
    }
}
